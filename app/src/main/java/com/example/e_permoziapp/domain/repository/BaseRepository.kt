package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.login.model.UserModel

interface BaseRepository {
    suspend fun getDataUser(userId: Int): Result<UserModel?>
}