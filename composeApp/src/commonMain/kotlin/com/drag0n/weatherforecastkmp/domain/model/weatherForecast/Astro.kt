package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable

@Serializable
data class Astro(
    val sunrise: String,
    val sunset: String
)