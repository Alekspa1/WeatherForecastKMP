package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val pressure_mb: Double,
    val temp_c: Double,
    val time: String,
    val wind_kph: Double
)