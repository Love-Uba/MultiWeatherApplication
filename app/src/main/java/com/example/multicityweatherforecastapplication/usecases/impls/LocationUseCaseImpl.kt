package com.example.multicityweatherforecastapplication.usecases.impls

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.multicityweatherforecastapplication.usecases.interfaces.LocationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import javax.inject.Inject

class LocationUseCaseImpl @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationUseCase {

    override var requestForPermission: (() -> Unit)? = null
    override var didGetLocationPermission: (() -> Unit)? = null
    private var onLocationReceived: ((latitude: Double, longitude: Double) -> Unit)? = null
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {

            super.onLocationResult(p0)
            val lastLocation = p0.lastLocation
            onLocationReceived?.let { it(lastLocation.latitude, lastLocation.longitude) }
        }
    }

    override fun createLocationRequest(
        onLocationReceived: (latitude: Double, longitude: Double) -> Unit,
        onError: (Exception) -> Unit
    ) {
        this.onLocationReceived = onLocationReceived
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices
            .getSettingsClient(fusedLocationProviderClient.applicationContext)
            .checkLocationSettings(builder.build())
        client.addOnSuccessListener {
            startLocationUpdates(locationRequest)
        }.addOnFailureListener { e ->
            onError(e)
        }
    }


    override fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates(
        locationRequest: LocationRequest
    ) {
        if (ActivityCompat.checkSelfPermission(
                fusedLocationProviderClient.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestForPermission(locationRequest)
            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun requestForPermission(
        locationRequest: LocationRequest
    ) {
        didGetLocationPermission = {
            startLocationUpdates(locationRequest)
        }
        requestForPermission?.let { it() }

    }
}