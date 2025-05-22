package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.main.repository.BaseRepositoryImpl

class GetUserDataUseCase(
    private val baseRepositoryImpl: BaseRepositoryImpl,
    private val getUserIdUseCase: GetUserIdUseCase
) {
    suspend fun execute(): Result<UserModel?> {
        val userId = getUserIdUseCase.invoke()
        return baseRepositoryImpl.getDataUser(userId)
    }
}