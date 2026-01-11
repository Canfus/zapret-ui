package ru.zapret.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.*
import ru.zapret.config.AppColor
import ru.zapret.view.ZapretControls
import ru.zapret.viewmodel.ZapretViewModel

@Composable
fun MainScreen(vm: ZapretViewModel) {
    Surface(color = AppColor.White) {
        Row(Modifier.fillMaxSize().padding(16.dp)) {

            Box(Modifier.width(300.dp).fillMaxHeight()) {
                val scrollState = rememberScrollState()

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(end = 10.dp)
                        .verticalScroll(scrollState)
                ) {

                    if (!vm.isInstalled) {
                        Text("Управление", style = MaterialTheme.typography.h5, color = AppColor.Black)
                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = { vm.install() },
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Установить запрет")
                        }
                    } else {
                        ZapretControls(vm)
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(scrollState),
                    style = defaultScrollbarStyle().copy(
                        unhoverColor = Color.Black.copy(alpha = 0.12f),
                        hoverColor = Color.Black.copy(alpha = 0.50f)
                    )
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f).fillMaxHeight()) {
                Text("Логи системы", color = Color.Gray)
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    val state = rememberLazyListState()
                    LaunchedEffect(vm.logs.size) {
                        if (vm.logs.isNotEmpty()) state.animateScrollToItem(vm.logs.size - 1)
                    }

                    LazyColumn(state = state, modifier = Modifier.fillMaxSize()) {
                        items(vm.logs) { log ->
                            Text(
                                text = log,
                                color = if (log.contains("Ошибка", true)) Color(0xFFFF5252) else Color(0xFF8BC34A),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    IconButton(
                        onClick = vm::clearLogs,
                        Modifier.align(Alignment.TopEnd)
                            .padding(8.dp),
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }

                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(state)
                    )
                }
            }
        }
    }
}
