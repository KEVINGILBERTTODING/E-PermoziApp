package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel

interface UserRepository {
    fun saveUserId(userId: Int)
    fun getUserId(): Int
    fun saveIsLogged(logged: Boolean)
    fun getIsLogged(): Boolean
    fun saveRole(role: String)
    suspend fun getDataUser(userId: Int): Result<UserModel?>
    suspend fun updateProfile(userId: Int, name: String, email: String, password: String?, mobileNumber: String, ktp: FileSelectModel?): Result<Unit>
    suspend fun updatePhotoProfile(userId: Int, file: FileSelectModel): Result<Unit>
}