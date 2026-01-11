package features.zapret.domain

class AutoTestAnalyzer {
  private val bestConfigRegex = Regex("Best config:\\s", RegexOption.IGNORE_CASE)

  fun findBestConfig(logs: List<String>): String? {
    return logs.mapNotNull { line ->
      bestConfigRegex.find(line)?.groupValues?.get(1)
    }.lastOrNull()
  }
}