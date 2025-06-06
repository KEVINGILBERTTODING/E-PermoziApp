package com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.core.util.ImageHelper
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.SubmitPengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidateSubmitFilePengajuan
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidateSubmitPengajuan
import com.example.e_permoziapp.domain.usecase.persyaratan.GetPersyaratanByJenisIdUseCase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubmitPengajuanViewmodel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val validateSubmitFilePengajuan: ValidateSubmitFilePengajuan,
    private val validateSubmitPengajuan: ValidateSubmitPengajuan,
    private val getPersyaratanByJenisIdUseCase: GetPersyaratanByJenisIdUseCase,
    private val submitPengajuanUseCase: SubmitPengajuanUseCase
): ViewModel() {

    var jenisPerizinanId = 0
    private val _fileSelectedState = MutableStateFlow<UiState<FileSelectModel>>(UiState.Idle)
    val fileSelectedState: StateFlow<UiState<FileSelectModel>> = _fileSelectedState
    var persyaratanModelList = mutableListOf<PersyaratanPerizinanModel>()
    var currentPosId = Pair<Int, Int>(-1, -1) // [1] CURRENT POSITION [2] ID PERSYARATAN
    var fileSelectedModlList = mutableListOf<FileSelectModel>()
    private val _submitState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitState: StateFlow<UiState<Unit>> = _submitState
    private val _getPersyaratanState = MutableStateFlow<UiState<List<PersyaratanPerizinanModel>>>(
        UiState.Idle)
    val getPersyaratanState: StateFlow<UiState<List<PersyaratanPerizinanModel>>> = _getPersyaratanState

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

    fun getPersyaratan() {
        viewModelScope.launch(Dispatchers.IO) {
            _getPersyaratanState.emit(UiState.Loading)
            val response = getPersyaratanByJenisIdUseCase(jenisPerizinanId)
            response
                .onSuccess {
                    val dataFiltered = it.filter { it.name.lowercase().contains("ktp").not() }
                    persyaratanModelList.addAll(dataFiltered)
                    _getPersyaratanState.emit(UiState.Success(dataFiltered))
                }
                .onFailure {
                    _getPersyaratanState.emit(UiState.Error(it.message ?: Constant.somethingWrong))

                }
        }
    }

    fun validateSubmitForm() {
        viewModelScope.launch {
            _submitState.emit(UiState.Loading)
            val userId = getUserIdUseCase.invoke()
            val filePersyaratan = fileSelectedModlList.filter {
                !it.key.isNullOrEmpty()
            }
            val validateFilePengajuan = validateSubmitFilePengajuan(persyaratanModelList, filePersyaratan)
            val validateSubmitPengajuan = validateSubmitPengajuan(userId, jenisPerizinanId, validateFilePengajuan.getOrNull())
            if (validateFilePengajuan.isFailure) {
                _submitState.emit(UiState.Error(validateFilePengajuan.exceptionOrNull()?.message ?: Constant.somethingWrong))
                return@launch
            }
            if (validateSubmitPengajuan.isFailure) {
                _submitState.emit(UiState.Error(validateSubmitPengajuan.exceptionOrNull()?.message ?: Constant.somethingWrong))
                return@launch
            }
            storePengajuan(userId, validateFilePengajuan.getOrNull()!!)
        }
    }

    suspend fun storePengajuan(userId: Int, filePengajuan: List<FileSelectModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = submitPengajuanUseCase.invoke(userId, jenisPerizinanId, filePengajuan)
            response
                .onSuccess { _submitState.emit(UiState.Success(Unit)) }
                .onFailure { _submitState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
        }
    }
}