package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel

interface PengajuanRepository {
    suspend fun getUserPengajuan(userId: Int): Result<List<PengajuanModel>?>
}