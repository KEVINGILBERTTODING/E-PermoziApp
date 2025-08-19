package com.example.e_permoziapp.domain.usecase.pengajuan

class ValidationGetPengajuanAEUseCase(
    private val filterDateValidationUseCase: FilterDateValidationUseCase
) {
    operator fun invoke(
        startDate: String?,
        endDate: String?,
        userId: Int,
        role: String): Result<Unit> {
        return try {
            val filterDateValidation = filterDateValidationUseCase(startDate, endDate)
            if (!filterDateValidation) return Result.failure(Exception(""))
            if (userId < 1) return Result.failure(Exception("User tidak valid"))
            if (role.isEmpty()) return Result.failure(Exception("Role tidak ditemukan"))
            Result.success(Unit)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}