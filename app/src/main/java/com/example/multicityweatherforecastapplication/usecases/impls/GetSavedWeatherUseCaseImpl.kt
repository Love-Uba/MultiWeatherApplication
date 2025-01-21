package com.example.multicityweatherforecastapplication.usecases.impls

import com.example.multicityweatherforecastapplication.data.WeatherRepository
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetSavedWeatherUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
) : GetSavedWeatherUseCase {

    override suspend fun fetchedAllSavedLocation(): Flow<Result<List<WeatherEntity>>> {
        return repository.fetchedAllSavedLocation()
    }

    override suspend fun setSavedStatus(savedFave: WeatherEntity, status: Boolean) {
        repository.setSavedStatus(savedFave, status)
    }

}