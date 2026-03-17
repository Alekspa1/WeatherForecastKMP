package com.drag0n.weatherforecastkmp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val city: String
)
