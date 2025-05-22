package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.core.util.FileHelper
import java.util.regex.Pattern

class ValidateEmailUseCase {
    operator fun invoke(email: String?): Result<String> {
        if (email.isNullOrEmpty()) {
            return Result.failure(Exception("Email tidak boleh kosong"))
        }
        if (!Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches()) {
            return Result.failure(Exception("Email tidak valid"))
        }
        return Result.success("success")
    }
}