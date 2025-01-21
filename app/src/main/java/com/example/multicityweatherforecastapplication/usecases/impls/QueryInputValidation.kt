package com.example.multicityweatherforecastapplication.usecases.impls

import com.example.multicityweatherforecastapplication.usecases.interfaces.InputValidationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

class QueryInputValidation: InputValidationUseCase {
    private fun validateQuery(query: String): Boolean = query.length >= 3

    override fun validateStringInput(inputFlow: Flow<String>): Flow<String> {
        return inputFlow
            .filter { validateQuery(it) }
            .debounce(500)
    }
}