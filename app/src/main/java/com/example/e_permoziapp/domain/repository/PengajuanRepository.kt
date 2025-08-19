package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.pengajuan.model.ResponsePengajuanAEModel
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import io.ktor.client.statement.HttpResponse

interface PengajuanRepository {
    suspend fun getUserPengajuan(userId: Int): Result<List<PengajuanModel>?>
    suspend fun getPengajuanDetail(id: Int): Result<UserPengajuanDetailModel>
    suspend fun updatePengajuan(userId: Int, pengajuanId: Int, jenisPerizinanId: Int,
                                filePersyaratanList: List<FileSelectModel>) : Result<Unit>
    suspend fun submitPengajuan(userId: Int, jenisPerizinanId: Int,
                                filePersyaratanList: List<FileSelectModel>) : Result<Unit>
    suspend fun destroyPengajuan(id: Int): Result<Unit>
    suspend fun getUserProfilePengajuan(userId: Int): Result<UserProfilePengajuanModel>
    suspend fun getAEPengajuan(startDate: String?, endDate: String?, userId: Int, role: String, idJenisPerizinan: Int?): Result<ResponsePengajuanAEModel>
    suspend fun replyPengajuan(aempId: Int, userId: Int,pengajuanId: Int, status: String, role: String, balasanFile: FileSelectModel?, balasanText: String?) : Result<Unit>

}