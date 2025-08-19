package com.example.e_permoziapp.data.perizinan.remote

import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.domain.remote.PerizinanService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class PerizinanServiceImpl(
    private val httpClient: HttpClient
): PerizinanService {
    override suspend fun getJenisPerizinan(): HttpResponse {
        return httpClient.get("${ServerInfo.BASE_URL}perizinan")
    }
}