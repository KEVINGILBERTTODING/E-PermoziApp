package com.example.e_permoziapp.domain.remote

import io.ktor.client.statement.HttpResponse

interface BaseService {
    suspend fun getDataUser(userId: Int): HttpResponse
}