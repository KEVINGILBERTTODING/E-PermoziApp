package com.example.e_permoziapp.data.main.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.remote.BaseService
import com.example.e_permoziapp.domain.repository.BaseRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class BaseRepositoryImpl(
    private val baseService: BaseService
): BaseRepository {
    override suspend fun getDataUser(userId: Int): Result<UserModel?> {
        return try {
            val response = baseService.getDataUser(userId)
            val responseBody = response.body<ResponseApiModel<UserModel>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    Result.success(responseBody.data)
                }
                else -> {
                    Result.failure(Exception(responseBody.message))
                }
            }
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}