package com.example.e_permoziapp.domain.remote

import com.example.e_permoziapp.data.pengajuan.model.PengajuanRequestModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import io.ktor.client.statement.HttpResponse

interface PengajuanService {
    suspend fun getPengajuanByUserId(params: PengajuanRequestModel) : HttpResponse
    suspend fun getPengajuanDetail(params: Int) : HttpResponse
    suspend fun updatePengajuan(userId: Int,pengajuanId: Int,jenisPerizinanId: Int,
                                 filePersyaratanList: List<FileSelectModel>) : HttpResponse
    suspend fun submitPengajuan(userId: Int, jenisPerizinanId: Int,
                                 filePersyaratanList: List<FileSelectModel>) : HttpResponse
    suspend fun destroyPengajuan(id: Int) : HttpResponse
    suspend fun getUserProfilePengajuan(userId: Int): HttpResponse
    suspend fun getAEPengajuan(startDate: String?, endDate: String?, userId: Int, role: String, idJenisPerizinan: Int?): HttpResponse
    suspend fun replyPengajuan(aempId: Int, userId: Int,pengajuanId: Int, status: String, role: String, balasanFile: FileSelectModel?, balasanText: String?) : HttpResponse
}