package com.drag0n.weatherforecastkmp.domain.useCases.permission

import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository

class IsGpsEnabledUseCase(private val repository : PermissionRepository) {
    operator fun invoke() = repository.isGpsEnabled()
}