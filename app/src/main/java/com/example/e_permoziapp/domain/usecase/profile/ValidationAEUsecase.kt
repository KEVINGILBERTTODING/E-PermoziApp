package com.example.e_permoziapp.domain.usecase.profile

import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import timber.log.Timber

class ValidationAEUsecase(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getRoleUseCase: GetRoleUseCase
) {
    operator fun invoke(): Result<Unit> {
        val userId = getUserIdUseCase()
        val role = getRoleUseCase()
        if (userId < 1) return Result.failure(Exception("User id tidak valid"))
        if (role.isEmpty()) return Result.failure(Exception("Role tidak valid"))
        if (role != "admin" && role != "employee") return Result.failure(Exception("Role tidak valid"))
        return Result.success(Unit)
    }
}