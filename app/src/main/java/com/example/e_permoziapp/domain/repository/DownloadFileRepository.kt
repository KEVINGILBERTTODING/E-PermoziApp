package com.example.e_permoziapp.domain.repository

import android.net.Uri

interface DownloadFileRepository {
    suspend fun downloadFile(url: String, filePath: String): Result<Uri>
}