package com.example.e_permoziapp.domain.usecase.persyaratan

import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.domain.repository.PersyaratanRepository

class GetPersyaratanByJenisIdUseCase(
    private val persyaratanRepository: PersyaratanRepository
) {
    suspend operator fun invoke(id: Int): Result<List<PersyaratanPerizinanModel>> {
        return persyaratanRepository.getPersyaratanByJenisPerizinanId(id)
    }
}