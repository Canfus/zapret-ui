package domain.model

data class ZapretConfig(
  val configName: String,
  val enableGameFilter: Boolean,
  val autoTests: Boolean
)
