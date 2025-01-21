package com.example.multicityweatherforecastapplication.data.models.baseweathermodel

data class Sys(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)