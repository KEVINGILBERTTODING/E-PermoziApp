package com.example.e_permoziapp.domain.usecase.pengajuan

import java.text.SimpleDateFormat
import java.util.Locale

class FilterDateValidationUseCase {
    operator fun invoke(startDate: String?, endDate: String?): Boolean {
        if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty()) return true
        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) return false

        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val start = formatter.parse(startDate)
            val end = formatter.parse(endDate)
            !end.before(start)
        } catch (e: Exception) {
            false
        }
    }
}