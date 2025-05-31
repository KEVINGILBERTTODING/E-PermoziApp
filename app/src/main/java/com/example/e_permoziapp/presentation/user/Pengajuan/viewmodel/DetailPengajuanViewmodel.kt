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
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.common.DownloadFileUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanDetailUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.UpdatePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidatePengajuanUseCase
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
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val validatePengajuanUseCase: ValidatePengajuanUseCase,
    private val updatePengajuanUseCase: UpdatePengajuanUseCase
): ViewModel() {
    private val _detailPengajuanState = MutableStateFlow<UiState<UserPengajuanDetailModel>>(UiState.Idle)
    val detailPengajuanState: StateFlow<UiState<UserPengajuanDetailModel>> = _detailPengajuanState
    private val _downloadState = MutableStateFlow<UiState<Uri>>(UiState.Idle)
    val downloadState: StateFlow<UiState<Uri>> = _downloadState
    private val _fileSelectedState = MutableStateFlow<UiState<FileSelectModel>>(UiState.Idle)
    val fileSelectedState: StateFlow<UiState<FileSelectModel>> = _fileSelectedState
    var pengajuanId = 0
    var currentPosId = Pair<Int, Int>(-1, -1) // [1] CURRENT POSITION [2] ID PERSYARATAN
    var fileSelectedModlList = mutableListOf<FileSelectModel>()
    private val _updateDataState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val updateDataState: StateFlow<UiState<Unit>> = _updateDataState


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

                val validateResponse = validateFileUploadUseCase(key, fileName, format, byteArray, true)
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

    fun validateUpdateData() {
        viewModelScope.launch {
            val userId = getUserIdUseCase.invoke()
            val dataPengajuan = if (detailPengajuanState.value is UiState.Success) {
                (detailPengajuanState.value as UiState.Success).data
            }else {
                null
            }
            val pengajuanId = dataPengajuan?.dataPengajuan?.id ?: -1
            val jenisPerizinanId = dataPengajuan?.dataJenisPersyaratan?.id ?: -1
            val filePersyaratanList = fileSelectedModlList.filter {
                it.key.isNullOrEmpty().not() && it.byteArray != null
            }

            if (filePersyaratanList.isEmpty()) {
                return@launch _updateDataState.emit(UiState.Error("Anda belum memilih file"))
            }

            filePersyaratanList.forEach{
                Timber.w("file list= ${it.filename}")
            }
            val validateResponse = validatePengajuanUseCase(userId, pengajuanId, jenisPerizinanId, true)
            if (validateResponse.isSuccess) {
                updateData(userId, pengajuanId, jenisPerizinanId, filePersyaratanList)
            }else {
                _updateDataState.emit(UiState.Error(validateResponse.exceptionOrNull()?.message ?: Constant.somethingWrong))
            }
        }
    }

    fun updateData(userId: Int, pengajuanId: Int, jenisPerizinanId: Int,
                           filePersyaratanList: List<FileSelectModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateDataState.emit(UiState.Loading)
            val response = updatePengajuanUseCase(userId, pengajuanId, jenisPerizinanId, filePersyaratanList)
            if (response.isSuccess) {
                _updateDataState.emit(UiState.Success(Unit))
            }else {
                _updateDataState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
            }
        }

    }
}