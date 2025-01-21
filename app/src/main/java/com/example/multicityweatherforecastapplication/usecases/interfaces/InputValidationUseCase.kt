package com.example.multicityweatherforecastapplication.usecases.interfaces

import kotlinx.coroutines.flow.Flow

interface InputValidationUseCase {
    fun validateStringInput(inputFlow: Flow<String>): Flow<String>
}