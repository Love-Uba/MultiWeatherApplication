package com.example.multicityweatherforecastapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.models.LongLatData
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetForecastDataFromCoordinatesUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetSavedWeatherUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetWeatherDataFromCoordinatesUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.LocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullWeatherViewModel @Inject constructor(
    private val locationDataUseCase: GetWeatherDataFromCoordinatesUseCase,
    private val forecastDataUseCase: GetForecastDataFromCoordinatesUseCase,
    private val locationUseCase: LocationUseCase,
    private val savedWeatherUseCase: GetSavedWeatherUseCase
) : ViewModel() {

    var longitude: Double? = null
    var latitude: Double? = null
    private val _fetchWeatherByCordResponse = MutableLiveData<Result<WeatherEntity>>()
    private val _fetchForecastByCordResponse = MutableLiveData<Result<List<WeatherEntity>>>()

    val weatherDataResponse: LiveData<Result<WeatherEntity>> = _fetchWeatherByCordResponse
    val forecastDataResponse: LiveData<Result<List<WeatherEntity>>> = _fetchForecastByCordResponse
    var triggerRequestForPermission: (() -> Unit)? = null

    fun actionGetLocationWeather(lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            locationDataUseCase.getLocationWeatherDataFrom(long, lat).collect {
                _fetchWeatherByCordResponse.postValue(it)
            }
        }
    }

    fun actionGetLocationForecast(lat: Double, long: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            forecastDataUseCase.getLocationForecastDataFrom(long, lat).collect {
                _fetchForecastByCordResponse.postValue(it)
            }
        }
    }

    fun startLocationUpdates(
        onLocationReceived: (Double, Double) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (longitude != null && latitude != null) {
            onLocationReceived(latitude!!, longitude!!)
        }
        locationUseCase.requestForPermission = { triggerRequestForPermission?.let { it() }}
        locationUseCase.createLocationRequest(onLocationReceived, onError)
    }

    fun stopLocationUpdates() {
        locationUseCase.stopLocationUpdates()
    }

    fun triggerLocationFetch() {
        locationUseCase.didGetLocationPermission?.let { it() }
    }

    fun updateLatitudeAndLongitude(longLatData: LongLatData) {
        this.longitude = longLatData.long
        this.latitude = longLatData.lat
    }

    fun saveFaveWeather(savedFave: WeatherEntity, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            savedWeatherUseCase.setSavedStatus(savedFave, status)
        }
    }
}