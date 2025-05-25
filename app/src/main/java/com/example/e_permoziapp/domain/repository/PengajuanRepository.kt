package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel

interface PengajuanRepository {
    suspend fun getUserPengajuan(userId: Int): Result<List<PengajuanModel>?>
    suspend fun getPengajuanDetail(id: Int): Result<UserPengajuanDetailModel>
}