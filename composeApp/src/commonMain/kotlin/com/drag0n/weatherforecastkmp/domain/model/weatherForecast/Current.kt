package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val precip_mm: Double,
    val temp_c: Double,
    val wind_mph: Double
)