package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.domain.repository.SessionRepository

class LogoutUseCase(
    private val sessionRepository: SessionRepository,
    private val clearAllUserInfoUseCase: ClearAllUserInfoUseCase
) {
    suspend operator fun invoke() {
        clearAllUserInfoUseCase.invoke()
        sessionRepository.logOut()
    }
}