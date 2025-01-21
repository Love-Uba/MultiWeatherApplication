package com.example.multicityweatherforecastapplication.usecases.impls

import com.example.multicityweatherforecastapplication.data.WeatherRepository
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetForecastDataFromCoordinatesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForecastDataFromCoordinatesUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
): GetForecastDataFromCoordinatesUseCase {
    override suspend fun getLocationForecastDataFrom(
        longitude: Double,
        latitude: Double
    ): Flow<Result<List<WeatherEntity>>> {
        return repository.getForecastWeather(latitude, longitude)
    }
}