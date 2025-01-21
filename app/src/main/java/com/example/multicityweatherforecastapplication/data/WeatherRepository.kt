package com.example.multicityweatherforecastapplication.data

import android.util.Log
import com.example.multicityweatherforecastapplication.data.local.WeatherDatabaseRepository
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.models.searchlocationmodel.SearchLocationDataItem
import com.example.multicityweatherforecastapplication.data.wrapper.DataMapper
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val weatherDatabaseRepository: WeatherDatabaseRepository,
    private val dataMapper: DataMapper
) {

    suspend fun getCurrentLocationWeather(lat: Double, long: Double): Flow<Result<WeatherEntity>> {

        return flow {
            weatherDatabaseRepository.fetchWeatherEntity(lat, long).map {
                emit(Result.Success(it))
            }
            emit(Result.Loading)
            try {
                val response = apiService.getAllWeatherData(lat, long)
                if (response.isSuccessful) {
                    val currentReport = response.body()?.let { dataMapper.weatherMapper(it) }
                    currentReport?.let {
                        saveWeatherData(it)
                        emit(Result.Success(it))
                    } ?: emit(Result.Error("Weather data is Unavailable. Try again"))
                } else {
                    emit(Result.Error(response.message()))
                }

            } catch (ex: Exception) {
                Log.d("DOGCATCHER", "catch: ${ex.localizedMessage} ")
                emit(Result.Error(ex.localizedMessage ?: "Something went wrong."))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getForecastWeather(lat: Double, long: Double): Flow<Result<List<WeatherEntity>>> {

        return flow {
            weatherDatabaseRepository.fetchForecastWeather(lat, long).map {
                emit(Result.Success(it))
            }
            emit(Result.Loading)

            try {
                val response = apiService.getForecastData(lat, long)
                if (response.isSuccessful) {
                    val weekReport = response.body()?.list?.map {
                        dataMapper.forecastMapper(response.body()!!.city, it)
                    }
                    weekReport?.let {
                        saveForecastReport(it)
                        emit(Result.Success(it))

                    } ?: emit(Result.Error("Forecast data is Unavailable. Try again"))

                } else {
                    emit(Result.Error(response.message()))
                }

            } catch (ex: Exception) {
                Log.d("DOGCATCHER", "catch: ${ex.localizedMessage} ")
                emit(Result.Error(ex.localizedMessage ?: "Something went wrong."))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun searchLocationWeather(searchParam: String): Result<List<SearchLocationDataItem>> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.searchLocationWeather(searchParam)
                if (response.isSuccessful) {
                    response.body()?.let { Result.Success(it) }
                        ?: Result.Error("Failed to fetch Search data. Try again")
                } else {
                    Result.Error(response.message())
                }
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage ?: "Something went wrong.")
        }
    }


    private suspend fun saveForecastReport(weekWeather: List<WeatherEntity>) {
        weatherDatabaseRepository.addForecastDataToRepo(weekWeather)
    }

    suspend fun setSavedStatus(savedFave: WeatherEntity, status: Boolean) {
        savedFave.saved = status
        weatherDatabaseRepository.updateFavoriteWeatherDataEntitySaveStatus(savedFave)
    }

    suspend fun saveWeatherData(weatherData: WeatherEntity) {
        weatherDatabaseRepository.addWeatherEntityToRepo(weatherData)
    }

    suspend fun fetchedAllSavedLocation(): Flow<Result<List<WeatherEntity>>> {
        return flow {
            emit(Result.Loading)
            weatherDatabaseRepository.fetchSavedFavoriteWeatherEntityFromRepo().collect {
                emit(Result.Success(it))
            }
        }.flowOn(Dispatchers.IO)
    }

}