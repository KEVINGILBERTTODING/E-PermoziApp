package com.example.e_permoziapp.domain.remote

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import io.ktor.client.statement.HttpResponse

interface UserService {
    suspend fun getDataUser(userId: Int): HttpResponse
    suspend fun updateProfile(userId: Int, name: String, email: String, password: String?, mobileNumber: String, ktp: FileSelectModel?): HttpResponse
    suspend fun updatePhoto(userId: Int, file: FileSelectModel): HttpResponse
}