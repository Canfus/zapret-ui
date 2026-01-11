package ru.zapret.viewmodel

import domain.model.ZapretConfig
import domain.model.ZapretStatus
import domain.usecase.EnsureZapretInstalledUseCase
import domain.usecase.GetZapretStatusUseCase
import domain.usecase.StartZapretUseCase
import domain.usecase.StopZapretUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.zapret.infrastructure.process.ProcessRunner
import ru.zapret.logging.LogConfig
import ru.zapret.logging.ProcessLog

class ZapretViewModel(
  private val ensureInstalled: EnsureZapretInstalledUseCase,
  private val startZapret: StartZapretUseCase,
  private val stopZapret: StopZapretUseCase,
  private val getStatus: GetZapretStatusUseCase,
  private val processRunner: ProcessRunner
) {
  private val scope = CoroutineScope(
    SupervisorJob() + Dispatchers.IO
  )

  private val _state = MutableStateFlow<ZapretUiState>(ZapretUiState.Idle)
  val state: StateFlow<ZapretUiState> = _state.asStateFlow()

  private val _logsBuffer = MutableStateFlow<List<ProcessLog>>(emptyList())
  val logsBuffer: StateFlow<List<ProcessLog>> = _logsBuffer.asStateFlow()

  init {
    refreshInitialState()
    observeLogs()
  }

  fun start(config: ZapretConfig) {
    when (_state.value) {
      is ZapretUiState.Idle,
      is ZapretUiState.Stopped -> startInternal(config)

      else -> Unit
    }
  }

  fun stop() {
    when (_state.value) {
      is ZapretUiState.Running -> stopInternal()
      else -> Unit
    }
  }

  fun retry() {
    when (_state.value) {
      is ZapretUiState.Error -> refreshInitialState()
      else -> Unit
    }
  }

  fun clear() {
    scope.cancel()
  }

  fun clearLogs() {
    _logsBuffer.value = emptyList()
  }

  private fun refreshInitialState() {
    scope.launch {
      runCatching {
        val status = getStatus()

        when (status) {
          ZapretStatus.RUNNING ->
            _state.value = ZapretUiState.Running

          ZapretStatus.STOPPED ->
            _state.value = ZapretUiState.Stopped

          ZapretStatus.UNKNOWN ->
            _state.value = ZapretUiState.Idle
        }
      }.onFailure { exception ->
        _state.value = ZapretUiState.Error(exception.message ?: "Internal error")
      }
    }
  }

  private fun startInternal(config: ZapretConfig) {
    scope.launch {
      _state.value = ZapretUiState.Installing

      runCatching {
        ensureInstalled()
        startZapret(config)

        _state.value = ZapretUiState.Running
      }.onFailure { exception ->
        _state.value = ZapretUiState.Error(exception.message ?: "Internal error")
      }
    }
  }

  private fun stopInternal() {
    scope.launch {
      runCatching {
        stopZapret()
        _state.value = ZapretUiState.Stopped
      }.onFailure { exception ->
        _state.value = ZapretUiState.Error(exception.message ?: "Internal error")
      }
    }
  }

  private fun observeLogs() {
    scope.launch {
      processRunner.logs().collect { log ->
        _logsBuffer.update { current ->
          val updated = current + log

          if (updated.size > LogConfig.MAX_LOG_LINES) {
            updated.takeLast(LogConfig.MAX_LOG_LINES)
          } else {
            updated
          }
        }
      }
    }
  }
}