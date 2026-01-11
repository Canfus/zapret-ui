package ru.zapret.ui.checkbox

import androidx.compose.ui.unit.sp
import androidx.compose.material.Text
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.material.CheckboxDefaults

import ru.zapret.config.AppColor

@Composable
fun PrimaryCheckbox(
  label: String,
  checked: Boolean,
  onChange: (Boolean) -> Unit
) {
  Checkbox(
    checked = checked,
    onCheckedChange = onChange,
    colors = CheckboxDefaults.colors(
      checkedColor = AppColor.Primary,
      uncheckedColor = AppColor.DisabledBg,
      disabledColor = AppColor.DisabledBg
    )
  )
  Text(label, fontSize = 14.sp, color = AppColor.Black)
}