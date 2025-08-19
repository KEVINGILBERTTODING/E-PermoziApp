package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.domain.repository.UserRepository

class SaveIsFinishOnboardingUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke() {
        userRepository.saveFinishOnboarding()
    }
}