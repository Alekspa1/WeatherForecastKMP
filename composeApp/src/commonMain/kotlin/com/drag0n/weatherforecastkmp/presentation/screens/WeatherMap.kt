package com.drag0n.weatherforecastkmp.presentation.screens

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.presentation.others.configureNativeSettings
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

@Composable
fun WeatherMap(coord: Coord) {
    val url =
        "https://www.meteoblue.com/weather/maps/widget/?windAnimation=0&windAnimation=1&gust=0&satellite=0&cloudsAndPrecipitation=0&temperature=0&temperature=1&sunshine=0&extremeForecastIndex=0&geoloc=fixed&tempunit=C&windunit=m%252Fs&lengthunit=metric&zoom=4&autowidth=auto#coords=5/${coord.lat}/${coord.lon}&map=windAnimation~rainbow~auto~10%20m%20above%20gnd~none"
    val webViewState = rememberWebViewState(url)

    // Включаем JS кроссплатформенно
    webViewState.webSettings.isJavaScriptEnabled = true

    LaunchedEffect(coord) {
        webViewState.content = WebContent.Url(url)
    }

    WebView(
        state = webViewState,
        modifier = Modifier
            .fillMaxSize(),
            // Этот модификатор "съедает" жесты до того, как их увидит Pager или Drawer
//            .pointerInput(Unit) {
//                detectDragGestures(
//                    onDrag = { change, _ ->
//                        change.consume() // Поглощаем жест, чтобы он не ушел родителю
//                    }
//                )
//            },

        onCreated = { nativeView ->
            configureNativeSettings(nativeView)
        }
    )
}