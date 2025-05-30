package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel

interface PengajuanRepository {
    suspend fun getUserPengajuan(userId: Int): Result<List<PengajuanModel>?>
    suspend fun getPengajuanDetail(id: Int): Result<UserPengajuanDetailModel>
    suspend fun updatePengajuan(userId: Int, pengajuanId: Int, jenisPerizinanId: Int,
                                filePersyaratanList: List<FileSelectModel>) : Result<Unit>
}