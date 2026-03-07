package com.drag0n.weatherforecastkmp.domain.model.mapper

import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatHour

data class WeatherFormatWeek(
    val date: String = "День 1",
    val hours: List<WeatherFormatHour>
)
