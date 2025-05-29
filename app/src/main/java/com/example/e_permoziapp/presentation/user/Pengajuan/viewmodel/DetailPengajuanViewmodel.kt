package com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.core.util.ImageHelper
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.common.DownloadFileUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanDetailUseCase
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class DetailPengajuanViewmodel(
    private val getPengajuanDetailUseCase: GetPengajuanDetailUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase
): ViewModel() {
    private val _detailPengajuanState = MutableStateFlow<UiState<UserPengajuanDetailModel>>(UiState.Idle)
    val detailPengajuanState: StateFlow<UiState<UserPengajuanDetailModel>> = _detailPengajuanState
    private val _downloadState = MutableStateFlow<UiState<Uri>>(UiState.Idle)
    val downloadState: StateFlow<UiState<Uri>> = _downloadState
    private val _fileSelectedState = MutableStateFlow<UiState<FileSelectModel>>(UiState.Idle)
    val fileSelectedState: StateFlow<UiState<FileSelectModel>> = _fileSelectedState
    var pengajuanId = 0
    var currentPosId = Pair<Int, Int>(-1, -1) // CURRENT POSITION AND ID PERSYARATAN


     fun getDetailPengajuan(id: Int) {
         viewModelScope.launch(Dispatchers.IO) {
             _detailPengajuanState.emit(UiState.Loading)
             try {
                 val response = getPengajuanDetailUseCase.invoke(id).getOrThrow()
                 _detailPengajuanState.emit(UiState.Success(response))
             }catch (e: Exception) {
                 e.printStackTrace()
                 Timber.e(e.message)
                 _detailPengajuanState.emit(UiState.Error(e.message ?: Constant.somethingWrong))
             }
         }
    }

    fun download(url: String, fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _downloadState.emit(UiState.Loading)
            val response = downloadFileUseCase(url, fileName)
            if (response.isSuccess) {
                _downloadState.emit(UiState.Success(response.getOrThrow()))
            } else {
                _downloadState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
            }
        }
    }

    fun validateFileSelected(uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val key = currentPosId.second.toString()
                val fileName = FileHelper.getFileNameFromUri(uri, context)
                val format = FileHelper.getMimeTypeFromUri(context, uri)
                val byteArray = if (ImageHelper.isImageUri(context, uri)) ImageHelper.uriToBitmap(context, uri)
                else FileHelper.uriToByteArray(context, uri)

                val validateResponse = validateFileUploadUseCase.invoke(key, fileName, format, byteArray)
                if (validateResponse.isSuccess) {
                    val filePersyaratanFix = FileSelectModel(uri, fileName, format, byteArray, key)
                    _fileSelectedState.emit(UiState.Success(filePersyaratanFix))
                }else {
                    _fileSelectedState.emit(UiState.Error(validateResponse.exceptionOrNull()?.message ?: "File tidak valid"))
                }

            }catch (e: Exception) {
                e.printStackTrace()
                _fileSelectedState.emit(UiState.Error("File tidak valid"))
            }
        }
    }
}