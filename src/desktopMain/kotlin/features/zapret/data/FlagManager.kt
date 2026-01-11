package features.zapret.data

import core.data.PathProvider
import features.zapret.domain.ZapretFlag
import java.io.File

class FlagManager(
  private val paths: PathProvider
) {
  fun isEnabled(flag: ZapretFlag): Boolean {
    val file = File(paths.utilsDir, flag.fileName)

    return file.exists()
  }

  fun toggle(flag: ZapretFlag, enabled: Boolean) {
    val file = File(paths.utilsDir, flag.fileName)

    if (enabled) {
      if (!paths.utilsDir.exists()) paths.utilsDir.mkdirs()
      file.writeText("ENABLED")
    } else {
      if (file.exists()) file.delete()
    }
  }
}