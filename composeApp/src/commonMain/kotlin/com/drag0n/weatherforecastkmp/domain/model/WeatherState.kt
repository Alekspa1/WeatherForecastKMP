package com.drag0n.weatherforecastkmp.domain.model

import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather

sealed interface WeatherState {
    data object Loading : WeatherState
    data class Success(val weather: Weather) : WeatherState
    data class Error(
        val message: String,
        val isNetworkError: Boolean
    ) : WeatherState
}