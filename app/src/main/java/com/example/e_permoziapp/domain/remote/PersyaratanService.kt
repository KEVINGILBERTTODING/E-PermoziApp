package com.example.e_permoziapp.domain.remote

import io.ktor.client.statement.HttpResponse

interface PersyaratanService {
    suspend fun getPersyaratanByJenisPerizinanId(id: Int): HttpResponse
}