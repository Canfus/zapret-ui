package features.zapret.domain

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableStateFlow

import features.zapret.data.FlagManager
import features.zapret.data.StrategyManager

class ZapretStateService(
  private val flagManager: FlagManager,
  private val strategyManager: StrategyManager
) {
  private val _uiState = MutableStateFlow(ZapretUiState())
  val state = _uiState.asStateFlow()

  fun refreshState() {
    _uiState.value = ZapretUiState(
      isGameFilterEnabled = flagManager.isEnabled(ZapretFlag.GAME_FILTER),
      isIpsetEnabled = flagManager.isEnabled(ZapretFlag.IPSET),
      isAutoTestOnLoadEnabled = flagManager.isEnabled(ZapretFlag.AUTO_TEST_ON_LOAD),
      availableStrategies = strategyManager.getAvailableStrategies().map { it.name }
    )
  }
}

data class ZapretUiState(
  val isGameFilterEnabled: Boolean = false,
  val isIpsetEnabled: Boolean = false,
  val isAutoTestOnLoadEnabled: Boolean = false,
  val availableStrategies: List<String> = emptyList()
)