package com.example.multicityweatherforecastapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.multicityweatherforecastapplication.data.models.searchlocationmodel.SearchLocationDataItem
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetLocationFromQueryUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.InputValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val locationQueryUseCase: GetLocationFromQueryUseCase,
    private val inputValidatorUseCase: InputValidationUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val _fetchSearchLocationResponse =
        MutableLiveData<Result<List<SearchLocationDataItem>>>()

    val searchLocationDataResponse: LiveData<Result<List<SearchLocationDataItem>>> =
        _fetchSearchLocationResponse

    private fun actionSearchLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            inputValidatorUseCase.validateStringInput(searchQuery)
                .collect { validInput ->
                    locationQueryUseCase.searchLocationWeather(validInput)
                        .distinctUntilChanged()
                        .collectLatest {
                            _fetchSearchLocationResponse.postValue(it)
                        }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        actionSearchLocation()
    }
}