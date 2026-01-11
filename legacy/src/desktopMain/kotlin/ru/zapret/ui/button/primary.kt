package ru.zapret.ui.button

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment

import ru.zapret.config.AppColor

@Composable
fun PrimaryButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable() (BoxScope.() -> Unit)
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val isHovered by interactionSource.collectIsHoveredAsState()

  val backgroundColor = when {
    isPressed -> AppColor.PrimaryActive
    isHovered -> AppColor.PrimaryHover
    else -> AppColor.Primary
  }

  Box(
    modifier
      .clip(RoundedCornerShape(6.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        onClick = onClick
      )
      .background(backgroundColor)
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