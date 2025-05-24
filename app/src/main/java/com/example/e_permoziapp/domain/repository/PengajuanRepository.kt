package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanModel

interface PengajuanRepository {
    suspend fun getUserPengajuan(userId: Int): Result<List<UserPengajuanModel>>
}