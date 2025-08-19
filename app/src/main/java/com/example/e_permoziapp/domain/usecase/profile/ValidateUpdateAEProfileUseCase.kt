package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import timber.log.Timber

class ValidateUpdateAEProfileUseCase(
    private val validateFormTextUseCase: ValidateFormTextUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
) {
    operator fun invoke(name: String?, email: String?, password: String?, role: String, photoProfile: FileSelectModel?): Result<Unit> {
        val validateName = validateFormTextUseCase(name)
        val validateEmail = validateEmailUseCase(email)
        val validatePhoto = validateFileUploadUseCase(photoProfile?.key, photoProfile?.filename, photoProfile?.format, photoProfile?.byteArray, photoProfile != null)
        if (validateName.not()) return Result.failure(Exception("Nama lengkap tidak boleh kosong"))
        if (validateEmail.isFailure) return Result.failure(Exception(validateEmail.exceptionOrNull()?.message))
        if (validatePhoto.isFailure) return Result.failure(Exception(validatePhoto.exceptionOrNull()?.message))
        if (role != "admin" && role != "employee") return Result.failure(Exception("Role tidak valid"))
        if (password.isNullOrEmpty().not()) {
            val validatePassword = validatePasswordUseCase(password)
            if (validatePassword.isFailure) return Result.failure(Exception(validatePassword.exceptionOrNull()?.message))
        }
        return Result.success(Unit)
    }
}