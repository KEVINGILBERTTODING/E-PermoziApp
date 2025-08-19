package com.example.e_permoziapp.domain.remote

import io.ktor.client.statement.HttpResponse

interface AdminEmployeeService {
    suspend fun getProfile(userId: Int, role: String): HttpResponse
}