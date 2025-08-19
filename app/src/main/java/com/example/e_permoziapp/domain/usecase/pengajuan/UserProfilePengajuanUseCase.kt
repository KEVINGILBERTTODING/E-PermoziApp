package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class UserProfilePengajuanUseCase(
    private val repository: PengajuanRepository
) {
    suspend operator fun invoke(userId: Int): Result<UserProfilePengajuanModel> {
        if (userId < 1) return Result.failure(Exception("Invalid User Id"))
        return repository.getUserProfilePengajuan(userId)
    }
}