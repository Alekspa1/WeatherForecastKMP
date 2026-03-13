package com.drag0n.weatherforecastkmp.data.di

import androidx.room.Room
import com.drag0n.weatherforecastkmp.data.repository.LocationIosImpl
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import com.drag0n.weatherforecastkmp.room.AppDatabaseConstructor
import com.drag0n.weatherforecastkmp.room.WeatherDataBase
import com.drag0n.weatherforecastkmp.room.getRoomDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory


actual val moduleLocation = module {

 single {
    LocationIosImpl(get(named("IP_LOCATION")))
} binds arrayOf(LocationRepository::class, PermissionRepository::class)
}

actual fun platformDatabaseModule() = module {
    single<WeatherDataBase> {
        val dbFile = NSHomeDirectory() + "/weather.db"
        val builder = Room.databaseBuilder<WeatherDataBase>(
            name = dbFile,
            factory = { AppDatabaseConstructor.initialize() } // ОБЯЗАТЕЛЬНО для iOS
        )
        getRoomDatabase(builder)
    }
}
