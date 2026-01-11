package features.zapret.data

import core.data.ProcessExecutor
import kotlinx.coroutines.flow.toList
import java.io.File

class WindowsServiceRepositoryImpl(
  private val executor: ProcessExecutor
) {
  suspend fun getActiveConfig(): String? {
    val command = "reg query HKLM\\System\\CurrentControlSet\\Services\\zapret /v zapret-discord-youtube"

    return try {
      val output = executor.execute(File("C:\\"), command).toList()

      val result = output.find { it.contains("zapret-discord-youtube") } ?: return null

      val parts = result.split(Regex("\\s+"))
      val fileName = parts.lastOrNull()

      fileName?.takeIf { it.endsWith(".bat") }
    } catch (e: Exception) {
      return null
    }
  }

  suspend fun isServiceInstalled(): Boolean {
    return getActiveConfig() != null
  }
}