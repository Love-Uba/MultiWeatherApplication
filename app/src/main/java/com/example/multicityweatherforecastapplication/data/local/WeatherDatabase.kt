package com.example.multicityweatherforecastapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase(){
    abstract fun weatherDao() : WeatherDao

    companion object {
        var dbase = "weather_database"
    }
}