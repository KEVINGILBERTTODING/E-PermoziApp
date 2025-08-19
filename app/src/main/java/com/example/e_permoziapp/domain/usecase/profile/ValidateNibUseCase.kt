package com.example.e_permoziapp.domain.usecase.profile

class ValidateNibUseCase {
    operator fun invoke(nib: String?): Result<Unit> {
        if (nib.isNullOrEmpty()) return Result.failure(Exception("Nib tidak boleh kosong"))
        if (nib.length > 13) return Result.failure(Exception("Nib tidak boleh lebih dari 13 karakter"))
        if (nib.length < 13) return Result.failure(Exception("Nib tidak boleh kurang dari 13 karakter"))
        return Result.success(Unit)
    }
}