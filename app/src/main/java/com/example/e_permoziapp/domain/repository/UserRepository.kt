package com.example.e_permoziapp.domain.repository

interface UserRepository {
    fun saveUserId(userId: Int)
    fun getUserId(): Int
    fun saveIsLogged(logged: Boolean)
    fun getIsLogged(): Boolean
}