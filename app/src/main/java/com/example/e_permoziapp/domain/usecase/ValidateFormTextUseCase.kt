package com.example.e_permoziapp.domain.usecase

class ValidateFormTextUseCase {
    operator fun invoke(text: String?): Boolean {
        return text?.isNotBlank() == true
    }
}