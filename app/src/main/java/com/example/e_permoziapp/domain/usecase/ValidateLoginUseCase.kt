package com.example.e_permoziapp.domain.usecase

class ValidateLoginUseCase(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getIsLogginUseCase: GetIsLogginUseCase
) {
    operator fun invoke(): Boolean {
        return getUserIdUseCase() != null && getIsLogginUseCase()
    }
}