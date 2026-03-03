package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Weather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)