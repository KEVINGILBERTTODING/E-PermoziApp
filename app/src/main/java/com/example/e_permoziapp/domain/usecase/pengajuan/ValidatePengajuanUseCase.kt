package com.example.e_permoziapp.domain.usecase.pengajuan

class ValidatePengajuanUseCase() {
    operator fun invoke(userId: Int, pengajuanId: Int, jenisPerizinanId: Int, isEdit: Boolean): Result<String> {
        if (userId < 0) {
            return Result.failure(Exception("User id tidak valid"))
        }
        if (isEdit && pengajuanId < 0) {
            return Result.failure(Exception("Data pengajuan tidak valid"))
        }
        if (jenisPerizinanId < 0) {
            return Result.failure(Exception("Jenis perizinan tidak valid"))
        }
        return Result.success("success")
    }
}