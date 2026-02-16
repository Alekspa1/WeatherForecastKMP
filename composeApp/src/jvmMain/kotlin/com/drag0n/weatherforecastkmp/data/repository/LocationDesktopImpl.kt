package com.drag0n.weatherforecastkmp.data.repository

import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class LocationDesktopImpl(private val locationInIp: LocationRepository) : LocationRepository, PermissionRepository {
    override suspend fun getCurrentLocation(): Coord? {
    return locationInIp.getCurrentLocation()

    }

    override fun isPermissionGranted(permission: String): Boolean {
        return true
    }

    override fun isGpsEnabled(): Boolean {
        return true
    }
}