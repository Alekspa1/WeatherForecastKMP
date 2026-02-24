package com.drag0n.weatherforecastkmp.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drag0n.weatherforecastkmp.domain.model.WeatherState
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType
import kotlinx.coroutines.delay
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import weatherforecastkmp.composeapp.generated.resources.Res
import weatherforecastkmp.composeapp.generated.resources.compose_multiplatform
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun App(viewModel: MyViewModel = koinViewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.getLocationFun() // 👈 ВЫЗЫВАЕМ ЗДЕСЬ
    }

    MaterialTheme(
        // Если хочешь, чтобы вся тема была темной
        colorScheme = darkColorScheme(surface = Color(0xFF121212))
    ) {
        Scaffold(
            // Обязательно задаем темный фон здесь, чтобы убрать белый экран
            containerColor = Color(0xFF121212)
        ) { innerPadding ->

            val weather = viewModel.weatherFlow
            val isLoading by viewModel.isLoading.collectAsState()

            // Используем Crossfade для плавного перехода от загрузки к погоде
            Crossfade(
                targetState = weather,
                animationSpec = tween(600),
                modifier = Modifier.padding(innerPadding)
            ) { currentWeather ->
                println(currentWeather.toString())
                when (currentWeather) {
                    is WeatherState.Loading -> {
                        LoadingScreen()
                    }

                    is WeatherState.Success -> {
                        MainWeatherPager(isLoading = isLoading,
                            onSearchClick = {showDialog = true},
                            onRefreshClick = {viewModel.getWeather(currentWeather.data.location.name)})
                    }
                    is WeatherState.Error -> {
                        ErrorScreen(
                            currentWeather.message,
                            currentWeather.isNetworkError
                        ) {viewModel.locationState?.let {
                            viewModel.getWeather("${it.lat},${it.lon}")
                        } ?: viewModel.getLocationFun()
                             }
                    }
                }

            }

            if (showDialog) {
                CitySearchDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { city ->
                        viewModel.getWeather(city)
                        showDialog = false
                    }
                )
            }
        }
    }
}

fun getCurrentFormattedDate(): String {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val day = now.dayOfMonth

    // Ручной маппинг месяца (так как в библиотеке нет встроенного русского формата)
    val monthName = when (now.month) {
        Month.JANUARY -> "янв"
        Month.FEBRUARY -> "фев"
        Month.MARCH -> "мар"
        Month.APRIL -> "апр"
        Month.MAY -> "мая"
        Month.JUNE -> "июн"
        Month.JULY -> "июл"
        Month.AUGUST -> "авг"
        Month.SEPTEMBER -> "сен"
        Month.OCTOBER -> "окт"
        Month.NOVEMBER -> "ноя"
        Month.DECEMBER -> "дек"
        else -> ""
    }

    return "$day $monthName"
}

fun typewWeather(code: Int): WeatherType {
    return when (code) {
        //1009 -> insertBackground(R.drawable.img_pasm) // пасмурно
        1087, 1273, 1276, 1282 -> WeatherType.STORMY
        // гроза
        //1150, 1153, 1168, 1171 -> insertBackground(R.drawable.img_2_day) // морось
        1063, 1072, 1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201, 1240, 1243, 1246, 1249, 1252
            -> WeatherType.RAINY
        // дождь
        1066, 1069, 1114, 1117, 1204, 1207, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1255, 1258, 1261, 1264
            -> WeatherType.SNOWY
        // снег
        1030, 1147, 1135 -> WeatherType.FOGGY // туман
        1000 -> WeatherType.SUNNY // Чистое небо
        else -> WeatherType.CLOUDY
    }
}




