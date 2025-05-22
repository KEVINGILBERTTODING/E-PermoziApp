package com.example.e_permoziapp.data.register.repository

import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.domain.remote.RegisterService
import com.example.e_permoziapp.domain.repository.RegisterRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class RegisterRepositoryImpl(
    private val service: RegisterService
) : RegisterRepository {
    override suspend fun register(
        email: String,
        fullname: String,
        mobileNumber: String,
        password: String,
        ktp: ByteArray
    ): Result<ResponseApiModel<String?>> {
        return try {
            val response = service.register(email, fullname, mobileNumber, password, ktp)
            val body = response.body<ResponseApiModel<Nothing>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    return Result.success(body)
                }
                else -> {
                    return Result.failure(Exception(body.message))
                }
            }
        }catch (e: Exception) {
            return Result.failure(e)
        }

    }
}