package com.example.e_permoziapp.data.login.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.remote.LoginService
import com.example.e_permoziapp.domain.repository.LoginRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

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

    override suspend fun lognAE(email: String, password: String, role: String): Result<AEModel> {
       return try {
           val response = loginService.loginAe(email, password, role)
           val body = response.body<ResponseApiModel<AEModel>>()
           when(response.status) {
               HttpStatusCode.OK -> {
                   if(body.data != null) {
                       Result.success(body.data)
                   }else {
                       Result.failure(Exception(Constant.somethingWrong))
                   }
               }else -> {
                   Result.failure(Exception(body.message ?: Constant.somethingWrong))
               }
           }
       }catch (e: Exception) {
           e.printStackTrace()
           Result.failure(e)
       }
    }
}