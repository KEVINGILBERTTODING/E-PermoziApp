package com.example.e_permoziapp.data.login.repository

import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.login.remote.LoginService
import com.example.e_permoziapp.domain.repository.LoginRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import timber.log.Timber

class LoginRepositoryImpl(
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<UserModel> {
        return try {
            val response = loginService.login(email, password)
            val body = response.body<ResponseApiModel<UserModel>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    if (body.data != null) {
                        Result.success(body.data)
                    }else {
                        Result.failure(Exception(body.message))
                    }
                }
                else -> {
                    return Result.failure(Exception(body.message))
                }
            }
        }catch (e: Exception) {
            Result.failure(e)
        }

    }
}