package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.repository.UserRepository

class GetUserDataUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: Int): Result<UserModel?> {
        return userRepository.getDataUser(userId)
    }
}