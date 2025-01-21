package com.example.multicityweatherforecastapplication.data.local

import kotlinx.coroutines.flow.Flow

interface WeatherDatabaseRepository {

    suspend fun fetchSavedFavoriteWeatherEntityFromRepo(): Flow<List<WeatherEntity>>

    suspend fun fetchWeatherEntity(lat: Double, longitude: Double): Flow<WeatherEntity>

    suspend fun addWeatherEntityToRepo(weatherEntity: WeatherEntity)

    suspend fun addForecastDataToRepo(forecast: List<WeatherEntity>)

    suspend fun fetchForecastWeather(lat: Double, longitude: Double): Flow<List<WeatherEntity>>

    suspend fun updateFavoriteWeatherDataEntitySaveStatus(weatherEntity: WeatherEntity)

    suspend fun clearAllDataFromDatabase()
}