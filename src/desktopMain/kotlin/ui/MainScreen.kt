package ui

import androidx.compose.runtime.Composable
import presentation.MainViewModel

@Composable
fun MainScreen(vm: MainViewModel) {
  StateView(vm)
}
