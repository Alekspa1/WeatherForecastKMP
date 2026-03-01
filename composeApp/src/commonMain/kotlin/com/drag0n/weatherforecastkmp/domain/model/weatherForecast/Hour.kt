package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val pressure_mb: Double,
    val temp_c: Double,
    val time_epoch: String,
    val wind_mph: Double
)