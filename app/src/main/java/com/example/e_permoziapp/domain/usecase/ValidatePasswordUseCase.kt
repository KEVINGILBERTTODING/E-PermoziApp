package com.example.e_permoziapp.domain.usecase

import com.example.e_permoziapp.core.util.FileHelper
import java.util.regex.Pattern

class ValidatePasswordUseCase {
    operator fun invoke(password: String?): Result<String> {
        if (password.isNullOrEmpty()) {
            return Result.failure(Exception("Password tidak boleh kosong"))
        }
        if (password.length < 8) {
            return Result.failure(Exception("Password harus memiliki minimal 8 karakter"))
        }
        return Result.success("success")
    }
}