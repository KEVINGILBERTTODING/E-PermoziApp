package com.example.e_permoziapp.domain.usecase.pengajuan

import com.example.e_permoziapp.domain.Entity.FileSelectModel

class ValidateSubmitPengajuan {
    operator fun invoke(userId: Int, jenisPerizinanId: Int, filePengajuanList: List<FileSelectModel>?): Result<Unit>{
        if (userId <= 0) return Result.failure(Exception("User tidak valid"))
        if (jenisPerizinanId <= 0) return Result.failure(Exception("Jenis pengajuan tidak valid"))
        if (filePengajuanList.isNullOrEmpty()) return Result.failure(Exception("File pengajuan tidak boleh kosong"))
        return Result.success(Unit)
    }
}