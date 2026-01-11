package ru.zapret.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import ru.zapret.config.AppColor
import ru.zapret.ui.button.BorderedButton
import ru.zapret.ui.button.PrimaryButton
import ru.zapret.ui.checkbox.PrimaryCheckbox
import ru.zapret.ui.select.PrimarySelect
import ru.zapret.viewmodel.ZapretViewModel

@Composable
fun FeatureRow(label: String, initialValue: Boolean, onToggle: () -> Unit) {
  var checked by remember { mutableStateOf(initialValue) }

  Row(
    Modifier.fillMaxWidth().clickable {
      checked = !checked
      onToggle()
    },
    verticalAlignment = Alignment.CenterVertically
  ) {
    PrimaryCheckbox(label, checked, onChange = {
      checked = it
      onToggle()
    })
  }
}


@Composable
fun ZapretControls(vm: ZapretViewModel) {
  val currentConfig = remember { mutableStateOf<String?>(null) }

  val actions = listOf(
    "Статус" to vm::checkStatus,
    "Удалить" to vm::deleteCurrentConfig,
    "Диагностика" to vm::runDiagnostics,
    "Обновить IPSET" to vm::updateIpset,
    "Обновить Hosts" to vm::updateHosts,
    "Тесты" to vm::runTests
  )

  Column(Modifier.fillMaxWidth()) {
    Text("Конфигурация", color = AppColor.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(10.dp))

    PrimarySelect(
      title = "Файл конфига",
      placeholder = "Выберите конфиг",
      options = vm.configs,
      value = currentConfig.value,
      onChange = { currentConfig.value = it }
    )

    PrimaryButton(
      onClick = {
        if (vm.selectedConfig == currentConfig.value) {
          vm.deleteCurrentConfig()
        } else {
          vm.installConfigService()
        }
      },
      modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
    ) {
      Text(
        if (vm.selectedConfig == currentConfig.value) "Удалить конфиг"
        else "Установить конфиг",
        color = AppColor.White
      )
    }

    FeatureRow("Проверить обновления", vm.isCheckUpdatesEnabled, onToggle = vm::checkUpdateToggle)
    FeatureRow("Игровой фильтр", vm.isGameFilterEnabled, onToggle = vm::gameFilterToggle)
    FeatureRow("Автотесты при запуске", vm.isAutoTestingOnLoad, onToggle = vm::autoTestOnLoadToggle)

    BorderedButton(onClick = vm::changeIpset, Modifier.padding(vertical = 10.dp)) {
      Text("Режим ipset: ${vm.ipsetStatus}", color = AppColor.Black)
    }

    actions.chunked(2).forEach { row ->
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        row.forEach { (name, cmd) ->
          BorderedButton(
            onClick = cmd,
            Modifier.weight(1f).padding(vertical = 2.dp)
          ) {
            Text(name, color = AppColor.Black, fontSize = 11.sp)
          }
        }
      }
    }

    if (vm.recommendedConfig != null) {
      Text("Найден более эффективный конфиг", color = AppColor.Black)

      PrimaryButton(onClick = vm::applyRecommendedConfig, Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text("Применить", color = AppColor.White)
      }
    }
  }
}
