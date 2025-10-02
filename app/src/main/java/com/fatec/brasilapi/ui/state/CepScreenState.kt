package com.fatec.brasilapi.ui.state

import com.fatec.brasilapi.model.AddressResponse

data class CepScreenState(
    val inputCep: String = "",
    val addressResponse: AddressResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
