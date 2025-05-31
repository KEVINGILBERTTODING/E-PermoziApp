package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel

interface PerizinanRepository {
    suspend fun getJenisPerizinan(): Result<List<JenisPerizinanModel>>
}