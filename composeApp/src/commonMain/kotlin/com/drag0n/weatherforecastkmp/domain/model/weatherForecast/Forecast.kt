package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    val forecastday: List<Forecastday>
)