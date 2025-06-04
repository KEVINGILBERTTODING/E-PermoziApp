package com.example.e_permoziapp.domain.usecase.pengajuan

import java.time.Duration
import java.time.Instant

class ValidatePengajuanDate {
    operator fun invoke(timeStamp: String): Boolean {
        val inputTime = Instant.parse(timeStamp)
        val now = Instant.now()
        val duration = Duration.between(inputTime, now)
        return duration.toDays() <= 3
    }
}