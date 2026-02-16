package com.drag0n.weatherforecastkmp.domain.useCases

import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository

class GetCoordInIpUseCase(private val repository : LocationRepository)  {

    suspend operator fun invoke() = repository.getCurrentLocation()
}