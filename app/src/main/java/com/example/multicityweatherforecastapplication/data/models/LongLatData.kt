package com.example.multicityweatherforecastapplication.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LongLatData(
    val long: Double,
    val lat: Double
): Parcelable
