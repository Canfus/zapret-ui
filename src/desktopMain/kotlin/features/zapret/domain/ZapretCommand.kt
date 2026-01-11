package features.zapret.domain

sealed class ZapretCommand(val id: String) {
  data object Install : ZapretCommand("1")
  data object Remove : ZapretCommand("2")
  data object CheckStatus : ZapretCommand("3")
  data object RunDiagnostics : ZapretCommand("4")
  data object CheckUpdates : ZapretCommand("5")
  data object ToggleGameFilter : ZapretCommand("7")
  data object ToggleIpSet : ZapretCommand("8")
  data object UpdateIpSetList : ZapretCommand("9")
  data object UpdateHosts : ZapretCommand("10")
  data object RunAutoTests : ZapretCommand("11")
}

enum class ZapretFlag(val fileName: String) {
  GAME_FILTER("game_filter.enabled"),
  IPSET("ipset.enabled"),
  AUTO_TEST_ON_LOAD("auto_test_on_load.enabled"),
  AUTO_UPDATES("check_updates.enabled")
}