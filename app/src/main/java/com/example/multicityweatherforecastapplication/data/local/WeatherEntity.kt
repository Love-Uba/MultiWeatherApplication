package com.example.multicityweatherforecastapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_weather_data_table")
data class WeatherEntity(
    @PrimaryKey
    val id : String,
    val locationName :  String,
    val country: String,
    val longitude : Double,
    val latitude : Double,
    val date : Int,
    val status : String,
    val icon : String,
    val temp : Double,
    val humidity : Int,
    val wind: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    var saved: Boolean
)
