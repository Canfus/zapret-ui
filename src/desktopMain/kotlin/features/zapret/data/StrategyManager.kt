package features.zapret.data

import java.io.File

import core.data.PathProvider

class StrategyManager(
  private val paths: PathProvider
) {
  fun getAvailableStrategies(): List<File> {
    return paths.rootDir.listFiles { _, name ->
      name.startsWith("general") && name.endsWith(".bat")
    }?.toList().orEmpty()
  }
}