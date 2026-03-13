package com.drag0n.weatherforecastkmp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Query("SELECT * FROM City")
    fun getAllWeather(): Flow<List<City>>

    @Insert
    suspend fun insertCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)
}