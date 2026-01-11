package domain.model

data class AutoTestResult(
  val bestConfig: ZapretConfig,
  val logs: List<String> = listOf()
)