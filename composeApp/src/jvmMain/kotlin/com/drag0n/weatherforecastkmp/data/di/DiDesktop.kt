package com.drag0n.weatherforecastkmp.data.di


import com.drag0n.weatherforecastkmp.data.repository.LocationDesktopImpl
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val moduleLocation = module {
    single<LocationRepository> { LocationDesktopImpl(get(named("IP_LOCATION"))) }
    single<PermissionRepository> { LocationDesktopImpl(get(named("IP_LOCATION"))) }

}