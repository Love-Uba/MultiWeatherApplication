package com.example.multicityweatherforecastapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherEntity: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: List<WeatherEntity>)

    @Query("SELECT * FROM main_weather_data_table WHERE latitude = :lat AND longitude = :longitude ORDER BY date DESC LIMIT 1")
    fun fetchWeather(lat: Double, longitude: Double): Flow<WeatherEntity>

    @Query("SELECT * FROM main_weather_data_table WHERE latitude = :lat AND longitude = :longitude")
    fun fetchForecastWeather(lat: Double, longitude: Double): Flow<List<WeatherEntity>>

    @Update
    fun saveFaveWeatherData(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM main_weather_data_table WHERE saved")
    fun fetchSavedWeather(): Flow<List<WeatherEntity>>

    @Query("DELETE FROM main_weather_data_table")
    suspend fun clearAll()
}