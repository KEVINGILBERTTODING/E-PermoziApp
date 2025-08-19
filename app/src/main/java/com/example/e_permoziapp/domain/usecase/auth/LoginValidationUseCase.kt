package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.core.constant.Constant

class LoginValidationUseCase(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {
    operator fun invoke(email: String?, password: String?): Result<Unit> {
        val responseEmailValidation = validateEmailUseCase(email)
        val responsePasswordValidation = validatePasswordUseCase(password)
        if (responseEmailValidation.isFailure) {
            return Result.failure(Exception(responseEmailValidation.exceptionOrNull()?.message ?: Constant.somethingWrong))
        }
        if (responsePasswordValidation.isFailure) {
            return Result.failure(Exception(responsePasswordValidation.exceptionOrNull()?.message ?: Constant.somethingWrong))
        }
        return Result.success(Unit)
    }
}