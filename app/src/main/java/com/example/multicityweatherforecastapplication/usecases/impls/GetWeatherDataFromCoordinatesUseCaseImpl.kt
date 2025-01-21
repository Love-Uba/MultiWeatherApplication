package com.example.multicityweatherforecastapplication.usecases.impls

import com.example.multicityweatherforecastapplication.data.WeatherRepository
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetWeatherDataFromCoordinatesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherDataFromCoordinatesUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
): GetWeatherDataFromCoordinatesUseCase {

    override suspend fun getLocationWeatherDataFrom(
        longitude: Double,
        latitude: Double
    ): Flow<Result<WeatherEntity>> {
        return repository.getCurrentLocationWeather(latitude, longitude)
    }
}