package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.domain.repository.UserRepository

class SaveUserIdUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: Int) {
        userRepository.saveUserId(userId)
    }
}