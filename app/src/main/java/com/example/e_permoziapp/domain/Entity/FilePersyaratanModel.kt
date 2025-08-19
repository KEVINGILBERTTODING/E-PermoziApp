package com.example.e_permoziapp.domain.Entity

import android.net.Uri

data class FilePersyaratanModel(
    val url: String = "",
    val uri: Uri? = null,
    val fileName: String = "",
    val format: String = "",
    val isUri: Boolean = false
)
