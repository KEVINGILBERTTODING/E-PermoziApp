package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.domain.repository.UserRepository

class GetIsLogginUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean {
        return userRepository.getIsLogged()
    }
}