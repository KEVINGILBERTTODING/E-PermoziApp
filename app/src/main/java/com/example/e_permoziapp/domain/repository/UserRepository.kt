package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.login.model.UserModel

interface UserRepository {
    fun saveUserId(userId: Int)
    fun getUserId(): Int
    fun saveIsLogged(logged: Boolean)
    fun getIsLogged(): Boolean
    fun saveRole(role: String)
    suspend fun getDataUser(userId: Int): Result<UserModel?>
}