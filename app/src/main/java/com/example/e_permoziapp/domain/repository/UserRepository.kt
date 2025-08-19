package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel

interface UserRepository {
    fun saveUserId(userId: Int)
    fun getUserId(): Int
    fun saveIsLogged(logged: Boolean)
    fun getIsLogged(): Boolean
    fun saveRole(role: String)
    fun getRole(): String
    fun saveFinishOnboarding()
    fun getIsFinishOnBoarding(): Boolean
    suspend fun getDataUser(userId: Int): Result<UserModel?>
    suspend fun updateProfile(userId: Int, name: String, email: String, password: String?, mobileNumber: String, ktp: FileSelectModel?, nib: String, npwp: FileSelectModel?,
                              nik: String, placeOfBirth: String, dateOfBirth: String, gender: String, religion: String,
                              job: String, region: String, address: String): Result<Unit>
    suspend fun updateAeProfile(userId: Int, name: String, email: String, password: String?, role: String, photo: FileSelectModel?): Result<Unit>
    suspend fun updatePhotoProfile(userId: Int, file: FileSelectModel): Result<Unit>
    suspend fun getAEProfile(userId: Int, role: String): Result<AEModel>
}