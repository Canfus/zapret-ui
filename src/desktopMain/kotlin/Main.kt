import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.appModule
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import presentation.MainViewModel
import ui.MainScreen

fun main() = application {
  KoinApplication(application = {
    modules(appModule)
  }) {
    val viewModel = koinInject<MainViewModel>()

    Window(onCloseRequest = ::exitApplication, title = "Zapret control panel") {
      MainScreen(viewModel)
    }
  }
}