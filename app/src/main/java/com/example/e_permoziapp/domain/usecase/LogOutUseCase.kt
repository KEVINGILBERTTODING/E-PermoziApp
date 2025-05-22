package com.example.e_permoziapp.domain.usecase

class LogOutUseCase(
    private val clearAllUserInfoUseCase: ClearAllUserInfoUseCase
) {
    operator fun invoke() {
        clearAllUserInfoUseCase.invoke()
    }
 }