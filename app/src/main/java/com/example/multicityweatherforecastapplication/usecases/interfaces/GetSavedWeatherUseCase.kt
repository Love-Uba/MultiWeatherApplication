package com.example.multicityweatherforecastapplication.usecases.interfaces

import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface GetSavedWeatherUseCase {

    suspend fun fetchedAllSavedLocation(): Flow<Result<List<WeatherEntity>>>
    suspend fun setSavedStatus(savedFave: WeatherEntity, status: Boolean)
}