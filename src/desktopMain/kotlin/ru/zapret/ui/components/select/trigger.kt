package ru.zapret.ui.components.select

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource

import ru.zapret.ui.theme.AppColor

@Composable
fun SelectTrigger(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable() (BoxScope.() -> Unit)
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val isHovered by interactionSource.collectIsHoveredAsState()

  val borderColor = when {
    isPressed -> AppColor.OutlineActive
    isHovered -> AppColor.OutlineHover
    else -> AppColor.Outline
  }

  Box(
    modifier
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        onClick = onClick
      )
      .border(1.dp, borderColor, RoundedCornerShape(6.dp))
      .clip(RoundedCornerShape(6.dp))
      .background(AppColor.White)
      .padding(PaddingValues(horizontal = 24.dp, vertical = 10.dp))
  ) {
    Box(
      Modifier.fillMaxWidth(),
      contentAlignment = Alignment.Center
    ) {
      content()
    }
  }
}
