package com.drag0n.weatherforecastkmp.domain.model

import com.drag0n.weatherforecastkmp.domain.model.mapper.WeatherFormatWeek
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatDay

sealed interface WeatherState {
    data object Loading : WeatherState
    data class Success(
        val weather: WeatherFormatDay,
        val week: List<WeatherFormatWeek>
    ) : WeatherState
    data class Error(
        val message: String,
        val isNetworkError: Boolean
    ) : WeatherState
}