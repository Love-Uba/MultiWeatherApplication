package com.example.multicityweatherforecastapplication.data.models.searchlocationmodel

data class SearchLocationDataItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)