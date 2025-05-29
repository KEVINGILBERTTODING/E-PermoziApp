package com.example.e_permoziapp.domain.Entity

import android.net.Uri

data class FileSelectModel(
    val uri: Uri?,
    val filename: String? = "",
    val format: String? = "",
    val byteArray: ByteArray?,
    val key: String? = ""
)