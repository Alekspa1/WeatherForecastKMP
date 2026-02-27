package com.drag0n.weatherforecastkmp.presentation.others

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.drag0n.weatherforecastkmp.SharedConfig
import com.drag0n.weatherforecastkmp.domain.model.WeatherState
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

    MaterialTheme(
        // Если хочешь, чтобы вся тема была темной
        colorScheme = darkColorScheme(surface = Color(0xFF121212))
    ) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.fillMaxWidth(0.5f)) {
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
                containerColor = Color(0xFF121212),
                bottomBar = {
                    Box(modifier = Modifier.fillMaxWidth()
                        .navigationBarsPadding(),
                        contentAlignment = Alignment.Center){
                        YandexBannerAd(SharedConfig.YANDEX_BANNER_ID, Modifier.fillMaxWidth())
                    }

                }
            ) { innerPadding ->

                val weather = viewModel.weatherFlow
                val isLoading by viewModel.isLoading.collectAsState()

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
                            MainWeatherPager(
                                isLoading = isLoading,
                                onSearchClick = { showDialog = true },
                                onRefreshClick = { viewModel.getWeather(currentWeather.data.location.name) },
                                openDrawerlick = { scope.launch { drawerState.open() } },
                                currentWeather.data
                            )
                        }

                        is WeatherState.Error -> {
                            ErrorScreen(
                                currentWeather.message,
                                currentWeather.isNetworkError
                            ) {
                                viewModel.locationState?.let {
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

}









