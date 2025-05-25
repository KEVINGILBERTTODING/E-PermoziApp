package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class GetPengajuanDetailUseCase(
    private val repository: PengajuanRepository
) {
    suspend operator fun invoke(id: Int): Result<UserPengajuanDetailModel> {
        return repository.getPengajuanDetail(id)
    }
}