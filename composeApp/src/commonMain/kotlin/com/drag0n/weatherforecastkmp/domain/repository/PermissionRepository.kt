package com.drag0n.weatherforecastkmp.domain.repository

interface PermissionRepository {
    fun isPermissionGranted(permission: String): Boolean
    fun isGpsEnabled(): Boolean
}