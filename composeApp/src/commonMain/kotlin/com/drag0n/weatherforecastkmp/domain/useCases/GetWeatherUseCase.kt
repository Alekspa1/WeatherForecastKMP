package com.drag0n.weatherforecastkmp.domain.useCases

import com.drag0n.weatherforecastkmp.domain.repository.WeatherRepository

class GetWeatherUseCase(private val repository: WeatherRepository) {

    suspend operator fun invoke(name: String) = repository.getWeatherDay(name)
}