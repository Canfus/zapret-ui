package ru.zapret.ui.select

import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowDropDown

import ru.zapret.config.AppColor

@Composable
fun <T> PrimarySelect(
  title: String,
  placeholder: String,
  options: List<T>,
  value: T?,
  onChange: (T) -> Unit,
  modifier: Modifier = Modifier
) {
  var expanded by remember { mutableStateOf(false) }

  val imageVector = when (expanded) {
    true -> androidx.compose.material.icons.Icons.Default.ArrowDropUp
    else -> androidx.compose.material.icons.Icons.Default.ArrowDropDown
  }

  Text(
    title,
    color = AppColor.Black,
    fontSize = 12.sp,
    modifier = Modifier.padding(vertical = 8.dp)
  )
  Box {
    SelectTrigger(
      onClick = { expanded = !expanded },
      modifier = modifier.fillMaxWidth(),
    ) {
      Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = value?.toString() ?: placeholder,
          color = AppColor.Black,
          maxLines = 1
        )
        Icon(
          imageVector = imageVector,
          contentDescription = null,
          tint = AppColor.Black
        )
      }
    }
    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      modifier = Modifier.width(268.dp).background(AppColor.White)
    ) {
      options.forEach { item ->
        DropdownMenuItem(onClick = {
          onChange(item)
          expanded = false
        }) {
          Text(item.toString(), color = AppColor.Black)
        }
      }
    }
  }
}