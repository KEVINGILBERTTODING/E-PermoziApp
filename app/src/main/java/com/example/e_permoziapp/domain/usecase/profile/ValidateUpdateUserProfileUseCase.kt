package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateMobileNumberUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase

class ValidateUpdateUserProfileUseCase(
    private val validateFormTextUseCase: ValidateFormTextUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val validateMobileNumberUseCase: ValidateMobileNumberUseCase
) {
    operator fun invoke(name: String?, email: String?, password: String?, mobileNumber: String?, ktp: FileSelectModel?): Result<Unit> {
        val validateName = validateFormTextUseCase(name)
        val validateEmail = validateEmailUseCase(email)
        val validateMobileNumber = validateMobileNumberUseCase(mobileNumber)
        val validateKtp = validateFileUploadUseCase(ktp?.key, ktp?.filename, ktp?.format, ktp?.byteArray, ktp != null)
        if (validateName.not()) return Result.failure(Exception("Nama lengkap tidak boleh kosong"))
        if (validateEmail.isFailure) return Result.failure(Exception(validateEmail.exceptionOrNull()?.message))
        if (validateMobileNumber.isFailure) return Result.failure(Exception(validateMobileNumber.exceptionOrNull()?.message))
        if (validateKtp.isFailure) return Result.failure(Exception(validateKtp.exceptionOrNull()?.message))
        if (password.isNullOrEmpty().not()) {
            val validatePassword = validatePasswordUseCase(password)
            if (validatePassword.isFailure) return Result.failure(Exception(validatePassword.exceptionOrNull()?.message))
        }
        return Result.success(Unit)
    }
}