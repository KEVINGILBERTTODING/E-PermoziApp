package com.example.e_permoziapp.data.pengajuan.remote

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.pengajuan.model.PengajuanRequestModel
import com.example.e_permoziapp.domain.remote.PengajuanService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PengajuanServiceImpl(
    private val httpClient: HttpClient
): PengajuanService {
    override suspend fun getPengajuanByUserId(params: PengajuanRequestModel): HttpResponse {
        return httpClient.post("${Constant.BASE_URL}user/pengajuan") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
    }

    override suspend fun getPengajuanDetail(params: Int): HttpResponse {
        return httpClient.get("${Constant.BASE_URL}user/pengajuan/$params")
    }
}