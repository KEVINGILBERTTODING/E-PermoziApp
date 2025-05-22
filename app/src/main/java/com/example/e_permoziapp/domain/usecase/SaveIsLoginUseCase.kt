package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.domain.repository.UserRepository

class SaveIsLoginUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(isLogged: Boolean) {
        userRepository.saveIsLogged(isLogged)
    }
}