package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.core.constant.Constant

class SaveUserInfoUseCase(
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val saveIsLoginUseCase: SaveIsLoginUseCase,
    private val saveRoleUseCase: SaveRoleUseCase
) {
    operator fun invoke(userId: Int, isLogin: Boolean, role: String): Result<Unit> {
        return try {
            saveUserIdUseCase(userId)
            saveIsLoginUseCase(isLogin)
            saveRoleUseCase(role)
            Result.success(Unit)
        }catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception("Gagal menyimpan data pengguna"))
        }
    }
}