package ru.zapret.ui.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.zapret.config.AppColor

enum class ScrollDirection(axis: String) {
  Vertical("y"),
  Horizontal("x")
}

@Composable
fun View(
  modifier: Modifier = Modifier,
  scrollDirection: ScrollDirection = ScrollDirection.Vertical,
  content: @Composable() (BoxScope.() -> Unit)
) {
  val scrollState = rememberScrollState()
  val scrollbarAdapter = rememberScrollbarAdapter(scrollState)
  val scrollableModifier =
    if (scrollDirection == ScrollDirection.Vertical) modifier.verticalScroll(scrollState) else modifier.horizontalScroll(
      scrollState
    )

  Box(scrollableModifier) {
    content()

    if (scrollDirection == ScrollDirection.Vertical) {
      VerticalScrollbar(
        adapter = scrollbarAdapter,
        modifier = Modifier.align(Alignment.Center),
        style = defaultScrollbarStyle().copy(
          hoverColor = AppColor.White.copy(alpha = 0.50F),
          unhoverColor = AppColor.White.copy(alpha = 0.12F)
        )
      )
    } else {
      HorizontalScrollbar(
        adapter = scrollbarAdapter,
        modifier = Modifier.align(Alignment.Center),
        style = defaultScrollbarStyle().copy(
          hoverColor = AppColor.White.copy(alpha = 0.50F),
          unhoverColor = AppColor.White.copy(alpha = 0.12F)
        )
      )
    }
  }
}
