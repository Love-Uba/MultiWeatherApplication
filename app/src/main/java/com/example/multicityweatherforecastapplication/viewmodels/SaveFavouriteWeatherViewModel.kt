package com.example.multicityweatherforecastapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetSavedWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveFavouriteWeatherViewModel @Inject constructor(
    private val getSavedWeatherUseCase: GetSavedWeatherUseCase
) : ViewModel() {

    private val _fetchSavedWeatherData = MutableLiveData<Result<List<WeatherEntity>>>()

    val savedFaveWeatherDataResponse: LiveData<Result<List<WeatherEntity>>> = _fetchSavedWeatherData

    fun fetchSavedWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedWeatherUseCase.fetchedAllSavedLocation().collect {
                _fetchSavedWeatherData.postValue(it)
            }
        }
    }

}