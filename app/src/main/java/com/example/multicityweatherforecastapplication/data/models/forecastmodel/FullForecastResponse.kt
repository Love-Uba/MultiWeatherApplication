package com.example.multicityweatherforecastapplication.data.models.forecastmodel

import com.example.multicityweatherforecastapplication.data.models.baseweathermodel.FullWeatherResponse

data class FullForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastWeatherResponse>,
    val message: Int
)