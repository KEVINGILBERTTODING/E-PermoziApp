package com.example.e_permoziapp.data.user.remote

import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.remote.UserService
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

class UserServiceImpl(
    private val httpClient: HttpClient
): UserService {
    override suspend fun getDataUser(userId: Int): HttpResponse {
        return httpClient.get("${ServerInfo.BASE_URL}user/$userId")
    }
    override suspend fun updateProfile(
        userId: Int,
        name: String,
        email: String,
        password: String?,
        mobileNumber: String,
        ktp: FileSelectModel?
    ): HttpResponse {
        val response = httpClient.submitFormWithBinaryData(
            url = "${ServerInfo.BASE_URL}user/profile/update",
            formData = formData {
                append("user_id", userId)
                append("fullname", name)
                append("email", email)
                append("mobile_number", mobileNumber)
                if (password.isNullOrEmpty().not()) {
                    append("password", password!!)
                }
                if (ktp != null) {
                    append(ktp.key!!, ktp.byteArray!!, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${ktp.filename}")
                        append(HttpHeaders.ContentType, ktp.format!!)
                    })
                }

            }
        ){
            method = HttpMethod.Post
        }
        return response
    }

    override suspend fun updatePhoto(userId: Int, file: FileSelectModel): HttpResponse {
        return httpClient.submitFormWithBinaryData(
            url = "${ServerInfo.BASE_URL}user/profile/update/photo",
            formData = formData {
                append("user_id", userId)
                append(file.key!!, file.byteArray!!, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=${file.filename}")
                    append(HttpHeaders.ContentType, file.format!!)
                })
            }
        ){
            method = HttpMethod.Post
        }
    }
}