package ru.zapret

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import ru.zapret.di.AppContainer
import ru.zapret.ui.App

fun main() {
  val container = AppContainer()

  application {
    Window(
      onCloseRequest = {
        container.zapretViewModel.clear()
        exitApplication()
      },
      title = "Zapret control panel"
    ) {
      App(container.zapretViewModel)
    }
  }
}
