package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.UserRepository

class UpdateUserProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Int, name: String, email: String, password: String?, mobileNumber: String, ktp: FileSelectModel?): Result<Unit> {
        return repository.updateProfile(userId, name, email, password, mobileNumber, ktp)
    }
}