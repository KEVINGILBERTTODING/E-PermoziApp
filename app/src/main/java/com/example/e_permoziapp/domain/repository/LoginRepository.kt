package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.UserModel

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<UserModel>
}