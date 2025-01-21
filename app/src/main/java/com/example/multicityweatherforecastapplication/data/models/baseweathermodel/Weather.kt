package com.example.multicityweatherforecastapplication.data.models.baseweathermodel

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)