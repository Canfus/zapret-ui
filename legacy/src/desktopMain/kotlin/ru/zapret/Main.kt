package ru.zapret

import androidx.compose.runtime.remember
import androidx.compose.ui.window.*
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import ru.zapret.config.AppConfig
import ru.zapret.services.*
import ru.zapret.viewmodel.ZapretViewModel
import ru.zapret.ui.MainScreen
import java.io.File

fun main() = application {
  AppConfig.load()

  val workingDir = File(System.getenv("LOCALAPPDATA"), AppConfig.appDirName)

  val client = HttpClient {
    install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
  }

  val vm = remember {
    ZapretViewModel(DownloadService(client), ZapretService(workingDir))
  }

  Window(
    onCloseRequest = ::exitApplication,
    title = "Zapret Panel Control",
    state = rememberWindowState(width = 1000.dp, height = 650.dp)
  ) {
    MainScreen(vm)
  }
}
