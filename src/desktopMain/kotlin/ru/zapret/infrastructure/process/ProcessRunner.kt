package ru.zapret.infrastructure.process

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.zapret.logging.ProcessLog
import java.io.BufferedReader
import java.io.File

interface ProcessRunner {
  fun start(command: List<String>, workingDir: File? = null)
  fun stop(processName: String)
  fun isRunning(processName: String): Boolean
  fun logs(): Flow<ProcessLog>
}

class WindowsProcessRunner : ProcessRunner {
  private val scope = CoroutineScope(
    SupervisorJob() + Dispatchers.IO
  )

  private val _logs = MutableSharedFlow<ProcessLog>(extraBufferCapacity = 100)

  override fun logs(): Flow<ProcessLog> {
    return _logs.asSharedFlow()
  }

  override fun start(command: List<String>, workingDir: File?) {
    scope.launch {
      _logs.emit(
        ProcessLog(
          source = ProcessLog.Source.SYSTEM,
          message = "Starting process: ${command.joinToString(" ")}"
        )
      )

      val process = ProcessBuilder(command)
        .apply {
          if (workingDir != null) {
            directory(workingDir)
          }
          redirectErrorStream(true)
        }
        .start()

      readStream(process.inputStream.bufferedReader(), ProcessLog.Source.STDOUT)
      readStream(process.errorStream.bufferedReader(), ProcessLog.Source.STDERR)
    }
  }

  override fun stop(processName: String) {
    scope.launch {
      _logs.emit(
        ProcessLog(
          source = ProcessLog.Source.SYSTEM,
          message = "Stopping process: $processName"
        )
      )

      Runtime.getRuntime()
        .exec("taskkill /IM $processName /F")
    }
  }

  override fun isRunning(processName: String): Boolean {
    return Runtime.getRuntime()
      .exec("tasklist")
      .inputStream
      .bufferedReader()
      .readText()
      .contains(processName, true)
  }

  private fun readStream(reader: BufferedReader, source: ProcessLog.Source) {
    scope.launch {
      reader.useLines { lines ->
        lines.forEach { line ->
          _logs.emit(
            ProcessLog(source = source, message = line)
          )
        }
      }
    }
  }
}