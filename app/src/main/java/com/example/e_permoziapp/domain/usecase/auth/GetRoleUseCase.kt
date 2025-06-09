package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.domain.repository.UserRepository

class GetRoleUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): String {
        return userRepository.getRole()
    }
}