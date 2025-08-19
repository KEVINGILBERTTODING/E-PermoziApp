package com.example.e_permoziapp.data.persyaratan.remote

import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.domain.remote.PersyaratanService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class PersyaratanServiceImpl(
    private val httpClient: HttpClient
): PersyaratanService {
    override suspend fun getPersyaratanByJenisPerizinanId(id: Int): HttpResponse {
        return httpClient.get("${ServerInfo.BASE_URL}persyaratan"){
            url {
                parameters.append("jenis_perizinan_id", id.toString())
            }
        }
    }
}