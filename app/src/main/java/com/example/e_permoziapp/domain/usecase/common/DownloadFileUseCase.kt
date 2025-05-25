package com.example.e_permoziapp.domain.usecase.common

import com.example.e_permoziapp.core.util.DownloadHelper
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DownloadFileUseCase(
    private val downloadHelper: DownloadHelper
) {
    operator fun invoke(url: String, fileName: String): Flow<UiState<String>> = flow {
        emit(UiState.Loading)
        val result = runCatching { downloadHelper.downloadFile(url, fileName) }
        result.fold(
            onSuccess = { res -> emit(UiState.Success("Download berhasil")) },
            onFailure = { error -> emit(UiState.Error(error.message ?: "Download gagal")) }
        )
    }
}