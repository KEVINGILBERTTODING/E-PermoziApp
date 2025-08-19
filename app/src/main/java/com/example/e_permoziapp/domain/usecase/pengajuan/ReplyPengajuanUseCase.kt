package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class ReplyPengajuanUseCase(
    private val pengajuanRepository: PengajuanRepository
) {
    suspend operator fun invoke(aempId: Int, userId: Int, pengajuanId: Int, role: String, balasanFile: FileSelectModel?, balasanText: String?, isApprove: Boolean): Result<Unit> {
        val status = if (isApprove) "success" else "failed"
        return pengajuanRepository.replyPengajuan(aempId, userId, pengajuanId, status, role, balasanFile, balasanText)
    }
}