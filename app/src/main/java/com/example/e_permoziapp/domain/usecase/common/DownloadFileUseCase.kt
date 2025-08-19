package com.example.e_permoziapp.domain.usecase.common

import android.net.Uri
import com.example.e_permoziapp.domain.repository.DownloadFileRepository

class DownloadFileUseCase(
    private val repository: DownloadFileRepository
) {
    suspend operator fun invoke(url: String, fileName: String): Result<Uri> {
        return repository.downloadFile(url, fileName)
    }
}