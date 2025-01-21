package com.example.multicityweatherforecastapplication.data

import com.example.multicityweatherforecastapplication.data.models.baseweathermodel.FullWeatherResponse
import com.example.multicityweatherforecastapplication.data.models.forecastmodel.FullForecastResponse
import com.example.multicityweatherforecastapplication.data.models.searchlocationmodel.SearchLocationData
import com.example.multicityweatherforecastapplication.utils.UtilConstants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/data/2.5/weather")
    suspend fun getAllWeatherData(
        @Query("lat")lat: Double,
        @Query("lon")lon: Double,
        @Query("units") unit: String = "metric",
        @Query("appid")apiKey: String = API_KEY
    ) : Response<FullWeatherResponse>

    @GET("/data/2.5/forecast")
    suspend fun getForecastData(
        @Query("lat")lat: Double,
        @Query("lon")lon: Double,
        @Query("cnt")count: Int = 48,
        @Query("units") unit: String = "metric",
        @Query("appid")apiKey: String = API_KEY
    ) : Response<FullForecastResponse>

    @GET("/geo/1.0/direct")
    suspend fun searchLocationWeather(
        @Query("q")city: String,
        @Query("appid")apiKey: String = API_KEY
    ) : Response<SearchLocationData>

}
