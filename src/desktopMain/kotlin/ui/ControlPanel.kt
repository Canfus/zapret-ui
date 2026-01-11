package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import features.zapret.usecase.BootstrapState
import presentation.MainViewModel

@Composable
fun StateView(vm: MainViewModel) {
  val state by vm.uiState.collectAsState()

  Box(Modifier.fillMaxSize().padding(10.dp)) {
    when (val bootstrap = state.bootstrapState) {
      is BootstrapState.Checking -> {
        CircularProgressIndicator()
        Text("Проверка компонентов...", Modifier.padding(top = 8.dp))
      }

      is BootstrapState.Downloading -> {
        LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(bootstrap.message, Modifier.padding(8.dp))
      }

      is BootstrapState.Error -> {
        Text(bootstrap.message, color = MaterialTheme.colorScheme.error)
      }

      is BootstrapState.Ready, BootstrapState.RunningAutoTests -> {
        ConfigsRenderer(state.availableConfigs, state.selectedConfig) { vm.onConfigSelected(it) }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigsRenderer(
  configs: List<String>,
  selectedConfig: String?,
  onConfigSelected: (String) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  Column(horizontalAlignment = Alignment.Start) {
    Text("Выберите конфигурацию", style = MaterialTheme.typography.labelLarge)

    ExposedDropdownMenuBox(
      expanded = expanded,
      onExpandedChange = { expanded = it },
      modifier = Modifier.padding(vertical = 8.dp)
    ) {
      OutlinedTextField(
        value = selectedConfig ?: "Конфиг не выбран",
        onValueChange = {},
        readOnly = true,
        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
        modifier = Modifier.menuAnchor().padding(0.dp).fillMaxWidth(1F)
      )

      ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
      ) {
        configs.forEach {
          DropdownMenuItem(
            text = { Text(it) },
            onClick = {
              onConfigSelected(it)
              expanded = false
            }
          )
        }
      }
    }

    Button(
      onClick = {},
      enabled = selectedConfig != null,
      modifier = Modifier.padding(top = 8.dp)
    ) {
      Text("Установить конфиг")
    }
  }
}
