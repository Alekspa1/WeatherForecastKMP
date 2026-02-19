package com.drag0n.weatherforecastkmp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp

@Composable
fun CitySearchDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
        title = { Text(text = "Поиск города") },
        text = {
            Column {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Введите название") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .focusRequester(focusRequester)
                )
            }
        },
        confirmButton = {
            Button(
                enabled = text.isNotBlank(), // Валидация: кнопка активна только с текстом
                onClick = { onConfirm(text) }
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )

    // Запускаем фокус на поле ввода при появлении диалога
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}