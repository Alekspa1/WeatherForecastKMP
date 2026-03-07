package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import kotlinx.serialization.Serializable

@Serializable
data class Forecastday(
    val astro: Astro,
    val hour: List<Hour>,
    val date: String,
    )
