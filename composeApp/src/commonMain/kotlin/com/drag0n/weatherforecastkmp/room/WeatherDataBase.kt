package com.drag0n.weatherforecastkmp.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [City::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class) // Обязательно для KMP
abstract  class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): Dao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<WeatherDataBase>
): WeatherDataBase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<WeatherDataBase> {
    override fun initialize(): WeatherDataBase
}