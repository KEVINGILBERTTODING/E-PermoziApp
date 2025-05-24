package com.example.e_permoziapp.domain.usecase.auth

class ValidateLoginUseCase(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getIsLoginUseCase: GetIsLoginUseCase
) {
    operator fun invoke(): Boolean {
        return getUserIdUseCase() != null && getIsLoginUseCase()
    }
}