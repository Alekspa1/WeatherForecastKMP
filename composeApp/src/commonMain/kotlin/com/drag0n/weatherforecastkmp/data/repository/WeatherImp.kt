package com.drag0n.weatherforecastkmp.data.repository


import com.drag0n.weatherforecastkmp.SharedConfig
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Astro
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather
import com.drag0n.weatherforecastkmp.domain.repository.WeatherRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherImp(val ktor: HttpClient) : WeatherRepository {



    override suspend fun getWeatherDay(name: String): Result<Weather> = runCatching {

        ktor.get("https://api.weatherapi.com/v1/forecast.json") {
            parameter("q", name)
            parameter("key", SharedConfig.WEATHER_API_KEY)
        }.body()

    }

    override suspend fun getWeatherAstronomy(name: String): Result<Astro> = runCatching{
        ktor.get("https://api.weatherapi.com/v1/astronomy.json") {
            parameter("q", name)
            parameter("key", SharedConfig.WEATHER_API_KEY)
        }.body()
    }

}

