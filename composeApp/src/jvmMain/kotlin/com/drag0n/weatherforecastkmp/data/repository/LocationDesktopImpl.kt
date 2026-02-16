package com.drag0n.weatherforecastkmp.data.repository

import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository

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