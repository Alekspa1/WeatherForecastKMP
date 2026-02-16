package com.drag0n.weatherforecastkmp.domain.useCases.permission

import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository

class IsPermissionUseCase(private val repository : PermissionRepository) {
    operator fun invoke(permission: String) = repository.isPermissionGranted(permission)
}