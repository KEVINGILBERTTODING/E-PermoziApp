package com.example.e_permoziapp.domain.remote

import com.example.e_permoziapp.data.pengajuan.model.PengajuanRequestModel
import io.ktor.client.statement.HttpResponse

interface PengajuanService {
    suspend fun getPengajuanByUserId(params: PengajuanRequestModel) : HttpResponse
}