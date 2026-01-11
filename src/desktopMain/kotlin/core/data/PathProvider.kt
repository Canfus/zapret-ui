package core.data

import java.io.File

class PathProvider(
  private val config: ConfigLoader
) {
  private val appData = System.getenv("APPDATA") ?: System.getProperty("user.home")

  val rootDir = File(appData, config.zapretFolderName)

  val serviceBat = File(rootDir, "service.bat")
  val utilsDir = File(rootDir, "utils")
  val binDir = File(rootDir, "bin")

  val ipsetDir = File(rootDir, "ipset")

  fun getGeneralConfig(configName: String): File {
    return File(rootDir, configName)
  }

  fun isZapretInstalled(): Boolean = serviceBat.exists()

  fun createRootFolder() {
    if (!rootDir.exists()) {
      rootDir.mkdirs()
    }
  }
}