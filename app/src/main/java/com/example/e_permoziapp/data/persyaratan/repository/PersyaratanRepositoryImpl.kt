package com.example.e_permoziapp.data.persyaratan.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.domain.remote.PersyaratanService
import com.example.e_permoziapp.domain.repository.PersyaratanRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import timber.log.Timber

class PersyaratanRepositoryImpl(
    private val persyaratanService: PersyaratanService
): PersyaratanRepository {
    override suspend fun getPersyaratanByJenisPerizinanId(id: Int): Result<List<PersyaratanPerizinanModel>> {
        return try {
            val response = persyaratanService.getPersyaratanByJenisPerizinanId(id)
            val data = response.body<ResponseApiModel<List<PersyaratanPerizinanModel>>>()
            Timber.w("response persyaratan ${response.body<ResponseApiModel<List<PersyaratanPerizinanModel>>>()}")
            when(response.status) {
                HttpStatusCode.OK -> {
                    if (!data.data.isNullOrEmpty()) {
                        Result.success(data.data)
                    }else {
                        Result.failure(Exception(Constant.somethingWrong))
                    }
                }else -> {
                    Result.failure(Exception(data.message ?: Constant.somethingWrong))
                }
            }
        }catch (e: Exception) {
            e.printStackTrace()
            Result.failure(Exception(e.message ?: Constant.somethingWrong))
        }
    }
}