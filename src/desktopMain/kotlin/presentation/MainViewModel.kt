package presentation

import features.zapret.data.StrategyManager
import features.zapret.usecase.BootstrapState
import features.zapret.usecase.BootstrapZapretUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
  val bootstrapState: BootstrapState = BootstrapState.Checking,
  val availableConfigs: List<String> = emptyList(),
  val selectedConfig: String? = null
)

class MainViewModel(
  private val bootstrapUseCase: BootstrapZapretUseCase,
  private val strategyManager: StrategyManager
) {
  private val viewModelScope = CoroutineScope(Dispatchers.Main)

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState = _uiState.asStateFlow()

  init {
    checkInstallation()
  }

  fun checkInstallation() {
    viewModelScope.launch {
      bootstrapUseCase().collect { state ->
        _uiState.value = _uiState.value.copy(bootstrapState = state)

        if (state is BootstrapState.Ready) {
          loadConfigs()
        }
      }
    }
  }

  private fun loadConfigs() {
    val configs = strategyManager.getAvailableStrategies().map { it.name }

    _uiState.value = _uiState.value.copy(
      availableConfigs = configs,
      selectedConfig = configs.firstOrNull()
    )
  }

  fun onConfigSelected(name: String) {
    _uiState.value = _uiState.value.copy(selectedConfig = name)
  }
}