package ru.zapret.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zapret.ui.components.button.PrimaryButton
import ru.zapret.viewmodel.ZapretUiState

@Composable
fun ControlsPanel(
  state: ZapretUiState,
  onStart: () -> Unit,
  onStop: () -> Unit
) {
  Column(
    modifier = Modifier.width(300.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text("Zapret")

    Divider()

    Text("State: ${state::class.simpleName}")

    PrimaryButton(
      onClick = onStart,
      enabled = state is ZapretUiState.Idle || state is ZapretUiState.Stopped,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text("Start")
    }

    PrimaryButton(
      onClick = onStop,
      enabled = state is ZapretUiState.Running,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text("Stop")
    }
  }
}
