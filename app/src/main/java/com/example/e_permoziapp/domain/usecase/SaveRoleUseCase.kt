package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.data.login.repository.UserRepositoryImpl

class SaveRoleUseCase(
    private val userRepositoryImpl: UserRepositoryImpl
) {
    operator fun invoke(role: String) {
       userRepositoryImpl.saveRole(role)
    }
}