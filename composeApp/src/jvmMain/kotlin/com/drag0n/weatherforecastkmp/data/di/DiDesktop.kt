package com.drag0n.weatherforecastkmp.data.di

import com.drag0n.weatherforecastkmp.data.repository.LocationDesktopImpl
import com.drag0n.weatherforecastkmp.domain.model.useCases.IsGpsEnabledUseCase
import com.drag0n.weatherforecastkmp.domain.model.useCases.IsPermissionUseCase
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import com.drag0n.weatherforecastkmp.domain.useCases.GetCurrentLocationUseCase


import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val moduleLocation = module {
    // 1. Ваша текущая реализация локации
    single<LocationRepository> { LocationDesktopImpl(get(named("IP_LOCATION"))) }
    single<PermissionRepository> { LocationDesktopImpl(get(named("IP_LOCATION"))) }

    //factory<GetCurrentLocationUseCase> { GetCurrentLocationUseCase(get()) }
    //factory<IsGpsEnabledUseCase> {IsGpsEnabledUseCase()  }
    //factory<IsPermissionUseCase> {IsPermissionUseCase()  }
}