package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.data.pengajuan.model.ResponsePengajuanAEModel
import com.example.e_permoziapp.domain.remote.PengajuanService
import com.example.e_permoziapp.domain.repository.PengajuanRepository

class GetPengajuanAeUseCase(
    private val  pengajuanRepository: PengajuanRepository
) {
    operator suspend fun invoke(
        startDate: String?,
        endDate: String,
        userId: Int,
        role: String,
        idJenisPerizinan: Int): Result<ResponsePengajuanAEModel> {
        return pengajuanRepository.getAEPengajuan(startDate, endDate, userId, role, idJenisPerizinan)
    }
}