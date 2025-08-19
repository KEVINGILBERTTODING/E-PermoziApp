package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.common.model.ResponseApiModel

interface RegisterRepository {
    suspend fun register(email: String, fullname: String, mobileNumber: String, password: String)
    : Result<ResponseApiModel<String?>>
}