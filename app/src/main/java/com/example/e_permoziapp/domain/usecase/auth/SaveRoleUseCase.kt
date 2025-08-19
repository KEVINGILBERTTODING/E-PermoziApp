package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.data.login.repository.UserRepositoryImpl
import com.example.e_permoziapp.domain.repository.UserRepository

class SaveRoleUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(role: String) {
       userRepository.saveRole(role)
    }
}