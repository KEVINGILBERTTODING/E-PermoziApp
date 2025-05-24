package com.example.e_permoziapp.domain.usecase.auth

class ValidateFormTextUseCase {
    operator fun invoke(text: String?): Boolean {
        return text?.isNotBlank() == true
    }
}