package com.example.e_permoziapp.domain.remote

import io.ktor.client.statement.HttpResponse

interface PerizinanService {
    suspend fun getJenisPerizinan(): HttpResponse
}