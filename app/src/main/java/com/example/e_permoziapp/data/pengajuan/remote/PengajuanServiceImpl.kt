package com.example.e_permoziapp.data.pengajuan.remote

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.data.pengajuan.model.PengajuanRequestModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.remote.PengajuanService
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

class PengajuanServiceImpl(
    private val httpClient: HttpClient
): PengajuanService {
    override suspend fun getPengajuanByUserId(params: PengajuanRequestModel): HttpResponse {
        return httpClient.post("${ServerInfo.BASE_URL}user/pengajuan") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }
    }

    override suspend fun getPengajuanDetail(params: Int): HttpResponse {
        return httpClient.get("${ServerInfo.BASE_URL}user/pengajuan/$params")
    }

    override suspend fun updatePengajuan(
        userId: Int,
        pengajuanId: Int,
        jenisPerizinanId: Int,
        filePersyaratanList: List<FileSelectModel>
    ): HttpResponse {
        val response = httpClient.submitFormWithBinaryData(
            url = "${ServerInfo.BASE_URL}user/pengajuan/update",
            formData = formData {
                append("user_id", userId)
                append("id", pengajuanId)
                append("jenis_perizinan_id", jenisPerizinanId)
                for (file in filePersyaratanList) {
                    append(file.key!!, file.byteArray!!, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${file.filename}")
                        append(HttpHeaders.ContentType, file.format!!)
                    })
                }
            }
        ){
            method = HttpMethod.Post
        }
        return response
    }
}