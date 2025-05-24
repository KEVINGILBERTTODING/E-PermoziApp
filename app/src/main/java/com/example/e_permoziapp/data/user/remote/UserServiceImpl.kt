package com.example.e_permoziapp.data.user.remote

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.domain.remote.UserService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class UserServiceImpl(
    private val httpClient: HttpClient
): UserService {
    override suspend fun getDataUser(userId: Int): HttpResponse {
        return httpClient.get("${Constant.BASE_URL}user/$userId")
    }
}