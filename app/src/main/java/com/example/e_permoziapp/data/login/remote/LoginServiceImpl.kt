package com.example.e_permoziapp.data.login.remote

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.data.login.model.LoginRequestModel
import com.example.e_permoziapp.domain.remote.LoginService
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginServiceImpl(
    private val httpClient: HttpClient
) : LoginService {
    override suspend fun login(
        email: String,
        password: String
    ): HttpResponse {
     return httpClient.post("${ServerInfo.BASE_URL}user/login") {
         contentType(ContentType.Application.Json)
         setBody(LoginRequestModel(email, password))
     }
    }
}