package com.drag0n.weatherforecastkmp

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.drag0n.weatherforecastkmp.data.di.initKoin
import com.drag0n.weatherforecastkmp.presentation.others.App

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