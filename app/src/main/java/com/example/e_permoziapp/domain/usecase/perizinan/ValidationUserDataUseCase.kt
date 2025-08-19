package com.example.e_permoziapp.domain.usecase.perizinan

class ValidationUserDataUseCase {
    operator fun  invoke(ktp: String?, nib: String?, npwp: String?): Result<Unit> {
        if (!ktp.isNullOrEmpty() && !nib.isNullOrEmpty() && !npwp.isNullOrEmpty()) return Result.success(Unit)
        return Result.failure(Exception(""))
    }
}