package com.drag0n.weatherforecastkmp.domain.useCases

import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository

class GetCurrentLocationUseCase(val repository: LocationRepository) {

    suspend operator fun invoke() : Coord? = repository.getCurrentLocation()
}