package com.drag0n.weatherforecastkmp.presentation.others

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.drag0n.weatherforecastkmp.SharedConfig
import com.drag0n.weatherforecastkmp.domain.WeatherMapper
import com.drag0n.weatherforecastkmp.domain.model.WeatherState
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherColors
import com.drag0n.weatherforecastkmp.presentation.YandexBannerAd
import com.drag0n.weatherforecastkmp.presentation.screens.MainWeatherPager
import com.drag0n.weatherforecastkmp.presentation.stateScreens.ErrorScreen
import com.drag0n.weatherforecastkmp.presentation.stateScreens.LoadingScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun App(viewModel: MyViewModel = koinViewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val weather by viewModel.weatherFlow.collectAsState()
    val location by viewModel.stateLocation.collectAsState(initial = "")

    val weatherColors = remember(weather) {
        if (weather is WeatherState.Success){
            val weatherMapper = WeatherMapper.weatherData((weather as WeatherState.Success).weather)
            getWeatherColors(weatherMapper.weatherType, weatherMapper.is_day)
        } else WeatherColors.Default

    }

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    LaunchedEffect(Unit) {
        viewModel.getLocationFun() // 👈 ВЫЗЫВАЕМ ЗДЕСЬ
    }
    Box(modifier = Modifier.fillMaxSize()){
        BoxBackgroundCircle(weatherColors)
        ModalNavigationDrawer(
                drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                // Контент вашего меню (список городов, настройки и т.д.)
                Text("Меню управления", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Настройки") },
                    selected = false,
                    onClick = {})
            }
        }
        ) {
        Scaffold(

            // Обязательно задаем темный фон здесь, чтобы убрать белый экран
            containerColor = Color.Transparent,
            bottomBar = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .navigationBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    YandexBannerAd(SharedConfig.YANDEX_BANNER_ID, Modifier.fillMaxWidth())
                }

            }
        ) { innerPadding ->

            // Используем Crossfade для плавного перехода от загрузки к погоде
            Crossfade(
                targetState = weather,
                animationSpec = tween(600),
                modifier = Modifier.padding(innerPadding)
            ) { currentWeather ->

                when (currentWeather) {
                    is WeatherState.Loading -> {
                        LoadingScreen()
                    }

                    is WeatherState.Success -> {
                        Box(modifier = Modifier.fillMaxSize()){
                            MainWeatherPager(
                                onSearchClick = { showDialog = true },
                                onRefreshClick = { viewModel.newLocation(currentWeather.weather.location.name) },
                                openDrawerlick = { scope.launch { drawerState.open() } },
                                currentWeather.weather,
                                weatherColors
                            )
                        }

                    }

                    is WeatherState.Error -> {
                        ErrorScreen(
                            currentWeather.message,
                            currentWeather.isNetworkError
                        ) {
                            if (location.isNotEmpty()) viewModel.newLocation(location)
                            else viewModel.getLocationFun()
                        }
                    }
                }

            }

            if (showDialog) {
                CitySearchDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { city ->
                        viewModel.newLocation(city)
                        showDialog = false
                    }
                )
            }
        }
    }
    }




}









