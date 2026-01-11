package ru.zapret.viewmodel

import kotlinx.coroutines.*
import androidx.compose.runtime.*

import ru.zapret.services.*
import ru.zapret.config.AppConfig

class ZapretViewModel(val downloader: DownloadService, val zapret: ZapretService) {
  val logs = mutableStateListOf<String>()
  var isInstalled by mutableStateOf(zapret.isInstalled())
  var configs by mutableStateOf(zapret.getConfigs())

  var selectedConfig by mutableStateOf("")
  var recommendedConfig by mutableStateOf<String?>(null)
  var isTesting by mutableStateOf(false)

  var isGameFilterEnabled by mutableStateOf(false)
  var isCheckUpdatesEnabled by mutableStateOf(false)
  var ipsetStatus by mutableStateOf("loaded")
  var isAutoTestingOnLoad by mutableStateOf(false)

  private val jobLabelMap = mapOf(
    "2" to "Удаление конфига",
    "3" to "Проверка статуса конфига",
    "4" to "Запуск диагностики",
    "6" to "Изменение флага проверки обновлений",
    "7" to "Изменение флага игрового фильтра",
    "8" to "Смена режима ipset",
    "9" to "Обновление ipset",
    "10" to "Обновление файла hosts",
    "11" to "Запуск тестов"
  )

  init {
    refreshStatuses()

    if (isAutoTestingOnLoad && isInstalled) {
      runAutoDiscovery()
    }
  }

  fun refreshStatuses() {
    isGameFilterEnabled = zapret.isGameFilterEnabled()
    isCheckUpdatesEnabled = zapret.isCheckUpdatesEnabled()
    isAutoTestingOnLoad = zapret.isAutoTestOnLoadEnabled()
    ipsetStatus = zapret.getIpsetStatus()
    isInstalled = zapret.isInstalled()
    configs = zapret.getConfigs()

    val activeInSystem = zapret.getActiveConfig()
    if (activeInSystem != null && configs.contains(activeInSystem)) {
      selectedConfig = activeInSystem
    } else if (selectedConfig.isEmpty() && configs.isNotEmpty()) {
      selectedConfig = configs[0]
    }
  }

  fun runAutoDiscovery() {
    if (isTesting) return;

    CoroutineScope(Dispatchers.IO).launch {
      isTesting = true

      val currentConfig = zapret.getActiveConfig()

      if (zapret.isInstalled()) {
        log("Остановка активной службы для проведения автотестов...")

        zapret.executeMenuAction("2") { log(it) }
        delay(3000)
      }

      try {
        ProcessBuilder("taskkill", "/F", "/IM", "winws.exe", "/T").start().waitFor()
        delay(1000)
      } catch (e: Exception) { /* ignore */
      }

      val bestConfig = zapret.runAutotests { log(it) }

      if (bestConfig != null) {
        if (bestConfig.name != currentConfig) {
          recommendedConfig = bestConfig.name

          log("Найден более оптимальный конфиг: ${bestConfig.name} (${bestConfig.score}%)")
        } else {
          recommendedConfig = null

          currentConfig.let { zapret.installService(it) { message -> log(message) } }
        }
      } else {
        currentConfig.let {
          if (it != null) {
            zapret.installService(it) { message -> log(message) }
          }
        }
      }

      isTesting = false
      refreshStatuses()
    }
  }

  fun autoTestOnLoadToggle() {
    val state = !isAutoTestingOnLoad

    zapret.toggleAutoTestOnLoad(state)
    isAutoTestingOnLoad = state

    log("Автотесты при запуске ${if (state) "включены" else "выключены"}")
  }

  fun applyRecommendedConfig() {
    recommendedConfig?.let {
      selectedConfig = it
      installConfigService()
      recommendedConfig = null
    }
  }

  fun log(msg: String) {
    logs.add(msg)

    if (logs.size > AppConfig.maxLogLines) {
      logs.removeFirst()
    }
  }

  fun clearLogs() {
    logs.clear()
  }

  fun install() {
    CoroutineScope(Dispatchers.IO).launch {
      try {
        val url = downloader.getDownloadUrl(AppConfig.githubApiUrl)
        downloader.download(url, zapret.workingDir) { log(it) }
        isInstalled = zapret.isInstalled()
        configs = zapret.getConfigs()

        if (configs.isNotEmpty()) {
          selectedConfig = configs[0]
        }

        refreshStatuses()
      } catch (e: Exception) {
        log("Ошибка: ${e.message}")
      }
    }
  }

  fun execute(cmd: String) {
    CoroutineScope(Dispatchers.IO).launch {
      val logMessage = jobLabelMap[cmd] ?: "Выполнение действия №$cmd..."

      log(logMessage)
      zapret.executeMenuAction(cmd) { log(it) }

      delay(500)
      refreshStatuses()
    }

  }

  fun installConfigService() {
    if (selectedConfig.isEmpty()) {
      log("Необходимо выбрать конфиг")
      return
    }

    CoroutineScope(Dispatchers.IO).launch {
      log("Установка конфига $selectedConfig")
      zapret.installService(selectedConfig) { log(it) }
      refreshStatuses()
    }
  }

  fun changeIpset() {
    execute("8")
  }

  fun checkUpdateToggle() {
    execute("6")
  }

  fun gameFilterToggle() {
    execute("7")
  }

  fun deleteCurrentConfig() {
    execute("2")
  }

  fun checkStatus() {
    execute("3")
  }

  fun runDiagnostics() {
    execute("4")
  }

  fun updateIpset() {
    execute("9")
  }

  fun updateHosts() {
    execute("10")
  }

  fun runTests() {
    execute("11")
  }
}
