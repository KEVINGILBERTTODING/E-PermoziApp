package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class GetUserAllPengajuanUseCase(
    val pengajuanRepository: PengajuanRepository
) {
    suspend operator fun invoke(userId: Int): Result<List<UserPengajuanModel>> {
        if (userId < 1) {
            return Result.failure(Exception("Invalid user ID"))
        }
        return pengajuanRepository.getUserPengajuan(userId)
    }
}