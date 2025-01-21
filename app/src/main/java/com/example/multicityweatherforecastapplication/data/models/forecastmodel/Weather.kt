package com.example.multicityweatherforecastapplication.data.models.forecastmodel

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)