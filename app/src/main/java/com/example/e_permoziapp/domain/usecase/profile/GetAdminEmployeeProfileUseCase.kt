package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.domain.repository.UserRepository

class GetAdminEmployeeProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int, role: String): Result<AEModel> {
        return userRepository.getAEProfile(userId, role)
    }
}