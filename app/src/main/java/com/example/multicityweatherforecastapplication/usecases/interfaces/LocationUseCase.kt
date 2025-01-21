package com.example.multicityweatherforecastapplication.usecases.interfaces

interface LocationUseCase {
    fun createLocationRequest(
        onLocationReceived: (latitude: Double, longitude: Double) -> Unit,
        onError: (Exception) -> Unit
    )
    fun stopLocationUpdates()
    var requestForPermission: (() -> Unit)?
    var didGetLocationPermission: (() -> Unit)?
}