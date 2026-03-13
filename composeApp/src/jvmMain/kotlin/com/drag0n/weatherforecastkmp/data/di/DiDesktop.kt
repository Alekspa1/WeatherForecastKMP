package com.drag0n.weatherforecastkmp.data.di


import androidx.room.Room
import com.drag0n.weatherforecastkmp.data.repository.LocationDesktopImpl
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.PermissionRepository
import com.drag0n.weatherforecastkmp.room.WeatherDataBase
import com.drag0n.weatherforecastkmp.room.getRoomDatabase
import com.drag0n.weatherforecastkmp.room.AppDatabaseConstructor
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module
import java.io.File


actual val moduleLocation = module {
    single {
        LocationDesktopImpl(get(named("IP_LOCATION")))
    } binds arrayOf(LocationRepository::class, PermissionRepository::class)
}

actual fun platformDatabaseModule() = module {
    single<WeatherDataBase> {
        // Выбираем путь: база будет лежать в папке пользователя
        val dbFile = File(System.getProperty("user.home"), "weather.db")

        val builder = Room.databaseBuilder<WeatherDataBase>(
            name = dbFile.absolutePath,
            factory = { AppDatabaseConstructor.initialize() } // Для JVM тоже лучше указывать фабрику
        )

        // Наша общая функция из commonMain
        getRoomDatabase(builder)
    }
}
