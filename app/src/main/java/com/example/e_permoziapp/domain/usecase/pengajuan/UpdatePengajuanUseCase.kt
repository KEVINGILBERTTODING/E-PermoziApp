package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class UpdatePengajuanUseCase(
    private val pengajuanRepository: PengajuanRepository
) {
    suspend operator fun invoke(userId: Int, pengajuanId: Int, jenisPerizinanId: Int,
                                filePersyaratan: List<FileSelectModel>): Result<Unit> {
        return pengajuanRepository.updatePengajuan(userId, pengajuanId, jenisPerizinanId, filePersyaratan)
    }
}