package com.example.multicityweatherforecastapplication.usecases.interfaces

import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface GetForecastDataFromCoordinatesUseCase {
    suspend fun getLocationForecastDataFrom(longitude: Double, latitude: Double): Flow<Result<List<WeatherEntity>>>
}