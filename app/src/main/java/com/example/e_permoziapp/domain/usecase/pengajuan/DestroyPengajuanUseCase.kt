package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.domain.repository.PengajuanRepository

class DestroyPengajuanUseCase(
    private val pengajuanRepository: PengajuanRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return pengajuanRepository.destroyPengajuan(id)
    }
}