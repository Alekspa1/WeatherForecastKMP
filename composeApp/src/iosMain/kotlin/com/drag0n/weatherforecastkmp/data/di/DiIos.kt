package com.drag0n.weatherforecastkmp.data.di

import com.drag0n.weatherforecastkmp.data.repository.LocationIosImpl
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module


actual val moduleLocation = module {

 single {
    LocationIosImpl(get(named("IP_LOCATION")))
} binds arrayOf(LocationRepository::class, PermissionRepository::class)
}
