package core.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.File
import java.nio.charset.Charset

class ProcessExecutor {
  private val windowsCharset = Charset.forName("CP866")

  fun execute(workingDir: File, executable: String, input: List<String> = emptyList()): Flow<String> = callbackFlow {
    val pb = ProcessBuilder("cmd.exe", "/c", executable)
      .directory(workingDir)
      .redirectErrorStream(true)

    val process = pb.start()

    val inputJob = launch(Dispatchers.IO) {
      val writer = process.outputStream.bufferedWriter(windowsCharset)

      input.forEach { line ->
        writer.write(line)
        writer.newLine()
        writer.flush()

        delay(300)
      }
    }

    val outputJob = launch(Dispatchers.IO) {
      process.inputStream.bufferedReader(windowsCharset).useLines { lines ->
        lines.forEach { line ->
          trySend(line)
        }
      }

      val exitCode = process.waitFor()
      trySend("[PROCESS_FINISHED_WITH_CODE_$exitCode]")
      close()
    }

    awaitClose {
      inputJob.cancel()
      outputJob.cancel()

      if (process.isAlive) process.destroy()
    }
  }
}