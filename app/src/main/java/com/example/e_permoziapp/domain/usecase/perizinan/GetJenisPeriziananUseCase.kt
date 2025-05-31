package com.example.e_permoziapp.domain.usecase.perizinan

import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.domain.repository.PerizinanRepository

class GetJenisPeriziananUseCase(
    private val repository: PerizinanRepository
) {
    suspend operator fun invoke(): Result<List<JenisPerizinanModel>> {
        return repository.getJenisPerizinan()
    }
}