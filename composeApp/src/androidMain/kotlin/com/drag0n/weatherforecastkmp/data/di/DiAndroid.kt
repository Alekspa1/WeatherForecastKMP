package com.drag0n.weatherforecastkmp.data.di

import com.drag0n.weatherforecastkmp.data.repository.GetcoordInIpImpl
import com.drag0n.weatherforecastkmp.data.repository.LocationAndroidImpl
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val moduleLocation = module {
    single<LocationRepository> {
        LocationAndroidImpl(get(),get(named("IP_LOCATION")))
    }
    single<PermissionRepository> {
        LocationAndroidImpl(get(),get(named("IP_LOCATION")))
    }

}