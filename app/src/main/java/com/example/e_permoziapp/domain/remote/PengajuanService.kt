package com.example.e_permoziapp.domain.remote

import com.example.e_permoziapp.data.pengajuan.model.PengajuanRequestModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import io.ktor.client.statement.HttpResponse

interface PengajuanService {
    suspend fun getPengajuanByUserId(params: PengajuanRequestModel) : HttpResponse
    suspend fun getPengajuanDetail(params: Int) : HttpResponse
    suspend fun updatePengajuan( userId: Int,pengajuanId: Int,jenisPerizinanId: Int,
                                 filePersyaratanList: List<FileSelectModel>) : HttpResponse
}