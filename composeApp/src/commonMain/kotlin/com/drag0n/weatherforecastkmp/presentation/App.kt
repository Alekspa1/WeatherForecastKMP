package com.drag0n.weatherforecastkmp.presentation

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import weatherforecastkmp.composeapp.generated.resources.Res
import weatherforecastkmp.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun App(viewModel: MyViewModel = koinViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.getLocationFun() // 👈 ВЫЗЫВАЕМ ЗДЕСЬ
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Погода KMP") })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.getLocationFun()
                }) {
                    Icon(modifier = Modifier.size(50.dp), painter = painterResource(Res.drawable.compose_multiplatform), contentDescription = "Обновить")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val weather = viewModel.weatherFlow

                if (weather != null) {
                    Text(
                        text = "Город: ${weather.location.name}",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Температура: ${weather.forecast.forecastday[0].hour[0].temp_c}°C",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text("Определяем местоположение...")
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                }

                // Координаты из репозитория
                viewModel.locationState?.let { coord ->
                    Spacer(Modifier.height(24.dp))
                    Text("Координаты: ${coord.lat}, ${coord.lon}",
                        style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}




