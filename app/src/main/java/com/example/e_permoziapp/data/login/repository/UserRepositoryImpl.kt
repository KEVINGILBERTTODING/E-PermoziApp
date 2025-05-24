package com.example.e_permoziapp.data.login.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.PrefHelper
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.remote.UserService
import com.example.e_permoziapp.domain.repository.UserRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class UserRepositoryImpl(
    private val prefHelper: PrefHelper,
    private val userService: UserService
): UserRepository {

    override fun saveUserId(userId: Int) {
        prefHelper.putInt(Constant.userIdKey, userId)
    }

    override fun getUserId(): Int {
        return prefHelper.getInt(Constant.userIdKey)
    }

    override fun saveIsLogged(logged: Boolean) {
        prefHelper.putBoolen(Constant.isLoggedKey, logged)
    }

    override fun getIsLogged(): Boolean {
        return prefHelper.getBoolen(Constant.isLoggedKey)
    }

    override fun saveRole(role: String) {
        prefHelper.putString(Constant.roleKey, role)
    }

    override suspend fun getDataUser(userId: Int): Result<UserModel?> {
        if (userId < 1) {
            return Result.failure(Exception("Invalid user id"))
        }
        return try {
            val response = userService.getDataUser(userId)
            val body = response.body<ResponseApiModel<UserModel>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    Result.success(body.data)
                }
                else -> {
                    Result.failure(Exception("Failed to get data user"))
                }
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}