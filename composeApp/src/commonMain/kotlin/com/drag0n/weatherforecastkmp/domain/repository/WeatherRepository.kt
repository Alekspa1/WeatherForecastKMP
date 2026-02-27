package com.drag0n.weatherforecastkmp.domain.repository


import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather

interface WeatherRepository {

    suspend fun getWeatherDay(name: String) : Result<Weather>

}