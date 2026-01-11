package ru.zapret.ui.theme

import androidx.compose.ui.graphics.Color

data class AppColors(
  val White: Color,
  val Black: Color,
  val Primary: Color,
  val PrimaryHover: Color,
  val PrimaryActive: Color,
  val Outline: Color,
  val OutlineHover: Color,
  val OutlineActive: Color,
  val OutlineLight: Color,
  val Danger: Color,
  val Success: Color,
  val DisabledBg: Color,
  val DisabledText: Color
)

val AppColor = AppColors(
  White = Color(0xFFECEBEB),
  Black = Color(0xFF242424),
  Primary = Color(0xFF4453DE),
  PrimaryHover = Color(0xFF3A48C7),
  PrimaryActive = Color(0xFF4453DE),
  Outline = Color(0xFFC4C4C4),
  OutlineHover = Color(0xFFA0A0A0),
  OutlineActive = Color(0xFF5E5E5E),
  OutlineLight = Color(0xFFEDEBEB),
  Danger = Color(0xFFFF4B4B),
  Success = Color(0xFF84CD40),
  DisabledBg = Color(0xFFDFDCDC),
  DisabledText = Color(0xFF868686)
)