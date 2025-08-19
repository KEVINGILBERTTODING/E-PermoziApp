package com.example.e_permoziapp.core.extention

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.formatedDateToIndonesia(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date = inputFormat.parse(this)!!

        val outputFormat = SimpleDateFormat("d MMMM yyyy 'pukul' HH:mm", Locale("id", "ID"))
        outputFormat.format(date)
    } catch (e: Exception) {
        this
    }
}