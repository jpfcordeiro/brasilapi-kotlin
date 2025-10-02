package com.fatec.brasilapi.model

import kotlinx.serialization.Serializable

@Serializable
data class AddressResponse(
    val cep: String,
    val state: String? = null,
    val city: String? = null,
    val district: String? = null,
    val street: String? = null
)
