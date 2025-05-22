package com.example.e_permoziapp.domain.usecase

import android.util.Log
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.domain.repository.RegisterRepository

class RegisterUseCase(
    private val repository: RegisterRepository
) {
    operator suspend fun invoke(
        email: String,
        fullname: String,
        password: String,
        mobileNumber: String,
        ktp: ByteArray): Result<ResponseApiModel<String?>> {
        return repository.register(email, fullname, mobileNumber, password, ktp)
    }
}