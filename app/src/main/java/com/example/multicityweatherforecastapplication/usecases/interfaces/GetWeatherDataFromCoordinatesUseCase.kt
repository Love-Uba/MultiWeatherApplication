package com.example.multicityweatherforecastapplication.usecases.interfaces

import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface GetWeatherDataFromCoordinatesUseCase {
    suspend fun getLocationWeatherDataFrom(longitude: Double, latitude: Double): Flow<Result<WeatherEntity>>
}