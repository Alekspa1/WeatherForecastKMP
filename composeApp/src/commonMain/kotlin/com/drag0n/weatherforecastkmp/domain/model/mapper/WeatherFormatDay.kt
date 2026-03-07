package com.drag0n.weatherforecastkmp.domain.model.weatherForecast

import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType

data class WeatherFormatDay(
    val lat: String,
    val lon: String,
    val city: String,
    val date: String,
    val weatherType: WeatherType,
    val icon: String,
    val temp: String,
    val feelslike: String,
    val desc: String,
    val wind: String,
    val humidity: String,
    val pressure: String,
    val sunrise: String,
    val sunset: String,
    val is_day: Boolean
)