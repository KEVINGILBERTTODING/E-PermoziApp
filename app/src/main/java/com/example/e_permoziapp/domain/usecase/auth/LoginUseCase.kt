package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.repository.LoginRepository

class LoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend fun login(email: String, password: String): Result<UserModel> {
        return loginRepository.login(email, password)
    }
    suspend fun loginAe(email: String, password: String, role: String) : Result<AEModel> {
        return loginRepository.lognAE(email, password, role)
    }
}