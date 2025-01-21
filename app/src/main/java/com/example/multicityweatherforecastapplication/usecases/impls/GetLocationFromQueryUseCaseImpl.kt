package com.example.multicityweatherforecastapplication.usecases.impls

import com.example.multicityweatherforecastapplication.data.WeatherRepository
import com.example.multicityweatherforecastapplication.data.models.searchlocationmodel.SearchLocationDataItem
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetLocationFromQueryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetLocationFromQueryUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository
) : GetLocationFromQueryUseCase {

    override fun searchLocationWeather(searchParam: String): Flow<Result<List<SearchLocationDataItem>>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = repository.searchLocationWeather(searchParam)
                emit(response)
            } catch (e: Exception) {
                emit(Result.Error(e.localizedMessage ?: "Unknown Error"))
            }

        }.flowOn(Dispatchers.IO)
    }
}