package com.example.e_permoziapp.data.main.remote

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.domain.remote.BaseService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class BaseServiceImpl(
    private val httpClient: HttpClient
): BaseService {
    override suspend fun getDataUser(userId: Int): HttpResponse {
        return httpClient.get("${Constant.BASE_URL}user/$userId")
    }
}