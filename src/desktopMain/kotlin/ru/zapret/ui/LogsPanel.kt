package ru.zapret.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zapret.logging.ProcessLog
import ru.zapret.ui.components.button.BorderedButton

@Composable
fun LogsPanel(
  logs: List<ProcessLog>,
  onClear: () -> Unit
) {
  Column(Modifier.fillMaxHeight()) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
      Text("Logs")

      BorderedButton(onClick = onClear) {
        Text("Clear")
      }
    }

    Divider()

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(6.dp)) {
      logs.forEach {
        Text("[${it.source}] ${it.message}")
      }
    }
  }
}
