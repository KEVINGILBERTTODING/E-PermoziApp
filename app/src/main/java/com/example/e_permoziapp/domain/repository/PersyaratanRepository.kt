package com.example.e_permoziapp.domain.repository

import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel

interface PersyaratanRepository {
    suspend fun getPersyaratanByJenisPerizinanId(id: Int) : Result<List<PersyaratanPerizinanModel>>
}