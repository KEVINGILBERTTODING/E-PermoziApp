package com.example.e_permoziapp.data.login.remote

import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.UserModel
import io.ktor.client.statement.HttpResponse
import io.ktor.http.cio.Response

interface LoginService {
    suspend fun login(email: String, password: String) : HttpResponse
}