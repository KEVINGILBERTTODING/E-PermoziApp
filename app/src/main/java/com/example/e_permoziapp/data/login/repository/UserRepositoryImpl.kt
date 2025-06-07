package com.example.e_permoziapp.data.login.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.PrefHelper
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
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
    override suspend fun updateProfile(
        userId: Int,
        name: String,
        email: String,
        password: String?,
        mobileNumber: String,
        ktp: FileSelectModel?
    ): Result<Unit> {
        return try {
            val response = userService.updateProfile(userId, name, email, password, mobileNumber, ktp)
            val body = response.body<ResponseApiModel<Nothing>>()
            when(response.status) {
                HttpStatusCode.OK -> Result.success(Unit)
                else -> Result.failure(Exception(body.message ?: Constant.somethingWrong))
            }
        }catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun updatePhotoProfile(userId: Int, file: FileSelectModel): Result<Unit> {
        return try {
            val response = userService.updatePhoto(userId, file)
            val body = response.body<ResponseApiModel<Unit>>()
            when(response.status) {
                HttpStatusCode.OK -> Result.success(Unit)
                else  -> Result.failure(Exception(body.message ?: Constant.somethingWrong))
            }
        }catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}