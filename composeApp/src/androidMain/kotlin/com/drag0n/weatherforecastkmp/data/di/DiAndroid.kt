package com.drag0n.weatherforecastkmp.data.di

import android.content.Context
import androidx.room.Room
import com.drag0n.weatherforecastkmp.data.repository.LocationAndroidImpl
import com.drag0n.weatherforecastkmp.data.room.WeatherDataBase
import com.drag0n.weatherforecastkmp.data.room.getRoomDatabase
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module



actual val moduleLocation = module {
    single {
        LocationAndroidImpl(get(), get(named("IP_LOCATION")))
    } binds arrayOf(LocationRepository::class, PermissionRepository::class)
}

actual fun platformDatabaseModule(): Module = module {
    single<WeatherDataBase> {
        val context: Context = get()
        val dbFile = context.getDatabasePath("weather.db")

        val builder = Room.databaseBuilder<WeatherDataBase>(
            context = context,
            name = dbFile.absolutePath
        )

        // ВЫЗОВ ВАШЕЙ ФУНКЦИИ:
        getRoomDatabase(builder)
    }
}
