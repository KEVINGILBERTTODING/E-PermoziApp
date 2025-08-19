package com.example.e_permoziapp.data.login.model

import kotlinx.serialization.Serializable
@Serializable
data class LoginAERequestModel(
    private val email: String,
    private val password: String,
    private val role: String
)
