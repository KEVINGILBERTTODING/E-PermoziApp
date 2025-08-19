package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.domain.repository.UserRepository

class GetIsLoginUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean {
        return userRepository.getIsLogged()
    }
}