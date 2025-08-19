package com.example.e_permoziapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun logOut()
    val logOutEvent: Flow<Unit>
}