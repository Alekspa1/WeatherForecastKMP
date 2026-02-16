package com.drag0n.weatherforecastkmp.domain.useCases

import com.drag0n.weatherforecastkmp.domain.repository.WeatherRepository

class GetAstronomyUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(name: String) = repository.getWeatherAstronomy(name)
}