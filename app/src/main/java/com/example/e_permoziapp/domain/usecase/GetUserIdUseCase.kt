package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.domain.repository.UserRepository

class GetUserIdUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Int {
        return userRepository.getUserId()
    }
}