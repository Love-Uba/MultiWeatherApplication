package com.example.multicityweatherforecastapplication.usecases.interfaces

import com.example.multicityweatherforecastapplication.data.models.searchlocationmodel.SearchLocationDataItem
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import kotlinx.coroutines.flow.Flow

interface GetLocationFromQueryUseCase {
    fun searchLocationWeather(searchParam: String): Flow<Result<List<SearchLocationDataItem>>>
}