package ru.zapret.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.ZapretConfig
import ru.zapret.viewmodel.ZapretViewModel

@Composable
fun App(viewModel: ZapretViewModel) {
  val state by viewModel.state.collectAsState()
  val logs by viewModel.logsBuffer.collectAsState()

  Row(Modifier.fillMaxSize().padding(8.dp)) {
    ControlsPanel(
      state = state,
      onStart = {},
      onStop = viewModel::stop
    )

    Spacer(Modifier.width(8.dp))

    LogsPanel(
      logs = logs,
      onClear = viewModel::clearLogs
    )
  }
}
