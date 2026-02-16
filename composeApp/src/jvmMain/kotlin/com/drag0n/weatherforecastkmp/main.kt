package com.drag0n.weatherforecastkmp

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.drag0n.weatherforecastkmp.data.di.initKoin
import com.drag0n.weatherforecastkmp.presentation.App
import org.koin.compose.KoinContext

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Weather KMP",
    ) {
        Column {
            App()
        }


    }
}