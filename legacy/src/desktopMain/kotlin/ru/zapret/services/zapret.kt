package ru.zapret.services

import kotlinx.coroutines.*
import ru.zapret.config.AppConfig
import java.io.File
import java.nio.charset.Charset

class ZapretService(val workingDir: File) {

  data class ConfigScore(val name: String, val score: Int)

  fun getActiveConfig(): String? {
    return try {
      val process = Runtime.getRuntime().exec(
        "reg query HKLM\\System\\CurrentControlSet\\Services\\zapret /v zapret-discord-youtube"
      )
      val output = process.inputStream.bufferedReader().readText()
      val regex = Regex("zapret-discord-youtube\\s+REG_SZ\\s+(.*)")
      val match = regex.find(output)
      match?.groupValues?.get(1)?.trim()?.let { "$it.bat" }
    } catch (e: Exception) {
      null
    }
  }

  suspend fun runAutotests(onLog: (String) -> Unit): ConfigScore? = withContext(Dispatchers.IO) {
    val logs = mutableListOf<String>()
    val testScript = File(workingDir, "utils/test zapret.ps1")

    if (!testScript.exists()) return@withContext null

    var process: Process? = null

    try {
      val pb = ProcessBuilder(
        "powershell.exe",
        "-NoProfile",
        "-ExecutionPolicy",
        "Bypass",
        "-File",
        testScript.absolutePath
      )
        .directory(workingDir)
        .redirectErrorStream(true)

      process = pb.start()

      val writer = process.outputStream.bufferedWriter(Charset.forName(AppConfig.consoleEncoding))
      val reader = process.inputStream.bufferedReader(Charset.forName(AppConfig.consoleEncoding))

      while (isActive) {
        val line = reader.readLine() ?: break

        logs.add(line)
        onLog(line)

        if (line.contains("Select test", true)) {
          delay(500)
          writer.write("1\r\n")
          writer.flush()
        }

        if (line.contains("Press any key", true)) {
          delay(500)
          writer.write("\r\n")
          writer.flush()
        }
      }

      process.waitFor()
      writer.close()
    } catch (e: Exception) {
      if (e !is CancellationException) {
        onLog("Ошибка автотестов: ${e.message}")
      }
    } finally {
      process?.destroyForcibly()
    }

    parseBestConfig(logs)
  }

  private fun parseBestConfig(logs: List<String>): ConfigScore? {
    val availableConfigs = getConfigs()
    val results = mutableListOf<ConfigScore>()

    for (config in availableConfigs) {
      val baseName = config.removeSuffix(".bat")
      var maxScore = -1

      for (line in logs) {
        if (line.contains(baseName, ignoreCase = true)) {
          val percentMatch = Regex("(\\d+)%").find(line)

          if (percentMatch != null) {
            maxScore = maxOf(maxScore, percentMatch.groupValues[1].toInt())

            continue
          }

          val ratioMatch = Regex("(\\d+)/(\\d+)").find(line)

          if (ratioMatch != null) {
            val score = (ratioMatch.groupValues[1].toDouble() / ratioMatch.groupValues[2].toDouble() * 100).toInt()
            maxScore = maxOf(maxScore, score)

            continue
          }

          if (line.contains("SUCCESS", true) || line.contains("[+]", true)) {
            maxScore = maxOf(maxScore, 100)
          }
        }
      }

      if (maxScore > 0) {
        results.add(ConfigScore(config, maxScore))
      }
    }

    return results.maxByOrNull { it.score }
  }

  fun isInstalled() = File(workingDir, "service.bat").exists()

  fun isGameFilterEnabled(): Boolean {
    return File(workingDir, "utils/game_filter.enabled").exists()
  }

  fun isCheckUpdatesEnabled(): Boolean {
    return File(workingDir, "utils/check_updates.enabled").exists()
  }

  fun isAutoTestOnLoadEnabled(): Boolean {
    return File(workingDir, "utils/auto_test_on_load.enabled").exists()
  }

  fun toggleAutoTestOnLoad(enabled: Boolean) {
    val file = File(workingDir, "utils/auto_test_on_load.enabled")

    if (enabled) {
      file.parentFile.mkdirs()
      file.writeText("ENABLED")
    } else {
      if (file.exists()) file.delete()
    }
  }

  fun getIpsetStatus(): String {
    val listFile = File(workingDir, "lists/ipset-all.txt")

    if (!listFile.exists() || listFile.length() == 0L) return "any"

    val content = listFile.readText(Charset.forName("UTF-8"))

    return if (content.contains("203.0.113.113/32")) "none" else "loaded"
  }

  private fun isGarbageLine(line: String): Boolean {
    val l = line.trim()
    if (l.isEmpty()) return true

    val forbiddenPhrases = listOf(
      "=========",
      "v1.9.2",
      "Install Service",
      "Remove Services",
      "Check Status",
      "Run Diagnostics",
      "Check Updates",
      "Switch",
      "Update ipset",
      "Update hosts",
      "Run Tests",
      "Exit",
      "Enter choice",
      "Pick one of the options",
      "Input file index",
      "Press any key",
      "Requesting admin rights",
      "Started with admin rights"
    )

    if (forbiddenPhrases.any { l.contains(it, ignoreCase = true) }) return true

    if (l.matches(Regex("^\\d+\\..*"))) return true

    return false
  }

  fun getConfigs() = workingDir.listFiles { _, name ->
    name.endsWith(".bat") && !name.startsWith("service", true)
  }?.map { it.name } ?: emptyList()

  suspend fun installService(config: String, onLog: (String) -> Unit) = withContext(Dispatchers.IO) {
    val configs = getConfigs()
    val index = configs.indexOf(config) + 1

    if (index <= 0) {
      onLog("Конфиг не найден в списке")
      return@withContext
    }

    var process: Process? = null

    try {
      val pb = ProcessBuilder("cmd.exe", "/c", "service.bat", "admin")
        .directory(workingDir)
        .redirectErrorStream(true)

      process = pb.start()

      val writer = process.outputStream.bufferedWriter(Charset.forName(AppConfig.consoleEncoding))
      val reader = process.inputStream.bufferedReader(Charset.forName(AppConfig.consoleEncoding))

      launch {
        while (isActive) {
          val line = reader.readLine() ?: break

          if (!isGarbageLine(line)) onLog(line)
        }
      }

      delay(800)
      writer.write("1\r\n"); writer.flush()

      delay(500)
      writer.write("$index\r\n"); writer.flush()

      delay(2000)
      writer.write("\r\n"); writer.flush()

      delay(500)
      writer.write("0\r\n"); writer.flush()

      process?.waitFor()
    } finally {
      process?.destroyForcibly()
    }
  }

  suspend fun executeMenuAction(menuNumber: String, onLog: (String) -> Unit) = withContext(Dispatchers.IO) {
    var process: Process? = null

    try {
      val pb = ProcessBuilder("cmd.exe", "/c", "service.bat", "admin")
        .directory(workingDir)
        .redirectErrorStream(true)

      process = pb.start()

      val writer = process.outputStream.bufferedWriter(Charset.forName(AppConfig.consoleEncoding))
      val reader = process.inputStream.bufferedReader(Charset.forName(AppConfig.consoleEncoding))

      launch {
        while (isActive) {
          val line = reader.readLine() ?: break

          if (!isGarbageLine(line)) onLog(line)
        }
      }

      delay(800)
      writer.write("$menuNumber\r\n"); writer.flush()

      delay(2000)
      writer.write("\r\n"); writer.flush()

      delay(500)
      writer.write("0\r\n"); writer.flush()

      process?.waitFor()
    } finally {
      process?.destroyForcibly()
    }
  }
}
