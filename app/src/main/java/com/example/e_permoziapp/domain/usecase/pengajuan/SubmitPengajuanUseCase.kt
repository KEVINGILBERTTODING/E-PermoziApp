package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class SubmitPengajuanUseCase(
    private val pengajuanRepository: PengajuanRepository
) {
    suspend operator fun invoke(userId: Int, jenisPerizinanId: Int, filePengajuan: List<FileSelectModel>): Result<Unit> {
        return pengajuanRepository.submitPengajuan(userId, jenisPerizinanId, filePengajuan)
    }
}