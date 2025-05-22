package com.example.e_permoziapp.data.register.remote

import android.util.Log
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.domain.remote.RegisterService
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

class RegisterServiceImpl(
    private val service: HttpClient
) : RegisterService {
    override suspend fun register(
        email: String,
        fullname: String,
        mobileNumber: String,
        password: String,
        ktp: ByteArray
    ): HttpResponse {
        Log.d("service", "register: $password")
        val response = service.submitFormWithBinaryData(
            url = "${Constant.BASE_URL}user/register",
            formData = formData {
                append("full_name", fullname)
                append("email", email)
                append("password",password)
                append("mobile_number", mobileNumber)
                append("ktp", ktp, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=ktp.jpg")
                    append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString())
                })
            }
        ){
            method = HttpMethod.Post
        }
        return response
    }
}