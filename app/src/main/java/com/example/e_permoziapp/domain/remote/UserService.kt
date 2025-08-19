package com.example.e_permoziapp.domain.remote

import com.example.e_permoziapp.domain.Entity.FileSelectModel
import io.ktor.client.statement.HttpResponse

interface UserService {
    suspend fun getDataUser(userId: Int): HttpResponse
    suspend fun updateProfile(userId: Int, name: String, email: String, password: String?, mobileNumber: String, ktp: FileSelectModel?, nib: String, npwp: FileSelectModel?,
                            nik: String, placeOfBirth: String, dateOfBirth: String, gender: String, religion: String,
                              job: String, region: String, address: String): HttpResponse
    suspend fun updateAeProfile(userId: Int, name: String, email: String, password: String?, role: String, photo: FileSelectModel?): HttpResponse
    suspend fun updatePhoto(userId: Int, file: FileSelectModel): HttpResponse
    suspend fun getAeProfile(userId: Int, role: String): HttpResponse
}