package com.drag0n.weatherforecastkmp.domain.repository


import com.drag0n.weatherforecastkmp.domain.model.Coord


interface LocationRepository {
    suspend fun getCurrentLocation() : Coord?

}