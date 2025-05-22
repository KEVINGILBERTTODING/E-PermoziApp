package com.example.e_permoziapp.domain.remote

import com.example.e_permoziapp.data.common.model.ResponseApiModel
import io.ktor.client.statement.HttpResponse

interface RegisterService {
    suspend fun register(email: String, fullname: String, mobileNumber: String, password: String, ktp: ByteArray)
            : HttpResponse
}