package ru.zapret.viewmodel

import domain.model.ZapretStatus

sealed class ZapretUiState {
  object Idle : ZapretUiState()
  object Installing : ZapretUiState()
  object Running : ZapretUiState()
  object Stopped : ZapretUiState()
  data class Error(val message: String) : ZapretUiState()
}