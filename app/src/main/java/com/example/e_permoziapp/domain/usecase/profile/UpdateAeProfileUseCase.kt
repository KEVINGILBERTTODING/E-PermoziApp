package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.UserRepository

class UpdateAeProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Int, name: String, email: String, password: String?, role: String, photo: FileSelectModel?): Result<Unit> {
        return repository.updateAeProfile(userId, name, email, password, role, photo)
    }
}