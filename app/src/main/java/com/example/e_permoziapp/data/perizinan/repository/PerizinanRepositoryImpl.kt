package com.example.e_permoziapp.data.perizinan.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.domain.remote.PerizinanService
import com.example.e_permoziapp.domain.repository.PerizinanRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class PerizinanRepositoryImpl(
    private val perizinanService: PerizinanService
): PerizinanRepository {
    override suspend fun getJenisPerizinan(): Result<List<JenisPerizinanModel>> {
        return try {
            val response = perizinanService.getJenisPerizinan()
            val body = response.body<ResponseApiModel<List<JenisPerizinanModel>>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    if (!body.data.isNullOrEmpty()) {
                        Result.success(body.data)
                    }else {
                        Result.failure(Exception(body.message ?: Constant.somethingWrong))
                    }
                }else -> {
                    Result.failure(Exception(body.message ?: Constant.somethingWrong))
                }
            }
        }catch (e: Exception) {
            Result.failure(Exception(e.message ?: Constant.somethingWrong))
        }
    }
}