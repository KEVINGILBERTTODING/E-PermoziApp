package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class GetPengajuanByUserIdUseCase(
    val pengajuanRepository: PengajuanRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<PengajuanModel>?> {
        if (userId < 1) {
            return Result.failure(Exception("Invalid user ID"))
        }
        return pengajuanRepository.getUserPengajuan(userId)
    }
}