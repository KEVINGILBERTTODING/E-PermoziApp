package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.UserRepository

class UpdateUserPhotoUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int, file: FileSelectModel): Result<Unit> {
        if (userId < 1) return Result.failure(Exception("User Id tidak valid"))
        return userRepository.updatePhotoProfile(userId, file)
    }
}