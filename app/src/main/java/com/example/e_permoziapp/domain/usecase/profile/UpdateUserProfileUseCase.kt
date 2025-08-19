package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.repository.UserRepository

class UpdateUserProfileUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: Int, name: String, email: String, password: String?, mobileNumber: String, ktp: FileSelectModel?, nib: String, npwp: FileSelectModel?,
                                nik: String, placeOfBirth: String, dateOfBirth: String, gender: String, religion: String,
                                job: String, region: String, address: String): Result<Unit> {
        return repository.updateProfile(userId, name, email, password, mobileNumber, ktp, nib, npwp,
            nik, placeOfBirth, dateOfBirth, gender, religion, job, region, address)
    }
}