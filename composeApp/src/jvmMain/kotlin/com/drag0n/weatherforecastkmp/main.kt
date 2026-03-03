package com.drag0n.weatherforecastkmp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.drag0n.weatherforecastkmp.data.di.initKoin
import com.drag0n.weatherforecastkmp.presentation.others.App
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

fun main() = application {
    initKoin()

    val scope = rememberCoroutineScope()
    var downloadProgress by remember { mutableStateOf(0f) }
    var isInitialized by remember { mutableStateOf(false) }


    // 2. Запускаем инициализацию Chromium в фоне
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(
                builder = {
                    installDir(File("kcef-bundle"))
                    progress {
                        // Обновляем наше состояние при каждом шаге загрузки
                        onDownloading { progress ->
                            // progress приходит в формате Double (например, 45.5)
                            downloadProgress = progress.toFloat() / 100f
                        }
                        onInitialized {
                            isInitialized = true
                        }
                    }
                },
                onError = { it?.printStackTrace() }
            )
        }
    }

    Window(
        onCloseRequest = {
            // Запускаем очистку ресурсов
            scope.launch {
                try {
                    KCEF.dispose()
                } finally {
                    exitApplication() // Закрываем приложение ПОСЛЕ очистки
                }
            }
        },
        title = "Weather KMP",
    ) {
        if (!isInitialized) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    // Превращаем 0.45 в "45%"
                    val percent = (downloadProgress * 100).toInt()
                    val statusText = when {
                        isInitialized -> "Готово!"
                        percent >= 100 -> "Распаковка и запуск движка..."
                        percent > 0 -> "Загрузка движка карты: $percent%"
                        else -> "Подготовка к загрузке..."
                    }
                    if (percent >= 100 && !isInitialized) {
                        CircularProgressIndicator() // Без параметра progress он просто крутится
                    } else {
                        CircularProgressIndicator(progress = downloadProgress)
                    }
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Это займет некоторое время",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
        } else {
            App() // Показываем контент, когда готово
        }
    }
}