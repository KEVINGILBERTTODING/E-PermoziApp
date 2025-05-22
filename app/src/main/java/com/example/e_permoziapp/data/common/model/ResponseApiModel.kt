package com.example.e_permoziapp.data.common.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseApiModel<out T>(
    val message: String? = "",
    val data: T? = null
)
