package com.example.e_permoziapp.data.pengajuan.repository

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.common.model.ResponseApiModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanRequestModel
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.domain.remote.PengajuanService
import com.example.e_permoziapp.domain.repository.PengajuanRepository
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode

class PengajuanRepositoryImpl(
    private val pengajuanService: PengajuanService
): PengajuanRepository {
    override suspend fun getUserPengajuan(userId: Int): Result<List<PengajuanModel>?> {
        return try {
            if (userId < 1) return Result.failure(Exception("Invalid User Id"))
            val dataRequest = PengajuanRequestModel(userId)
            val response = pengajuanService.getPengajuanByUserId(dataRequest)
            val body = response.body<ResponseApiModel<List<PengajuanModel>?>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    Result.success(body.data)
                }else -> {
                    Result.failure(Exception(body.message ?: Constant.somethingWrong))
                }
            }

        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPengajuanDetail(id: Int): Result<UserPengajuanDetailModel> {
        return try {
            if (id < 1) return Result.failure(Exception("Invalid pengajuan id"))
            val response = pengajuanService.getPengajuanDetail(id)
            val body = response.body<ResponseApiModel<UserPengajuanDetailModel?>>()
            when(response.status) {
                HttpStatusCode.OK -> {
                    if (body.data != null) {
                        Result.success(body.data)
                    }else {
                        Result.failure(Exception(Constant.somethingWrong))
                    }
                }else -> {
                Result.failure(Exception(body.message ?: Constant.somethingWrong))
                }
            }

        }catch (e: Exception) {
            Result.failure(e)
        }
    }

}