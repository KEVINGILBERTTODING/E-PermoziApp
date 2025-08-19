package com.example.e_permoziapp.presentation.admin_employee.pengajuan.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.core.util.ImageHelper
import com.example.e_permoziapp.data.balasan.model.BalasanModel
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.common.DownloadFileUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.DestroyPengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanDetailUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ReplyPengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.UpdatePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidatePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidationReplyPengajuanUseCase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailPengajuanAeViewmodel(
    private val getPengajuanDetailUseCase: GetPengajuanDetailUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val validationReplyPengajuanUseCase: ValidationReplyPengajuanUseCase,
    private val updatePengajuanUseCase: UpdatePengajuanUseCase,
    private val destroyPengajuanUseCase: DestroyPengajuanUseCase,
    private val getRoleUseCase: GetRoleUseCase,
    private val replyPengajuanUseCase: ReplyPengajuanUseCase
): ViewModel() {
    private val _detailPengajuanState = MutableStateFlow<UiState<UserPengajuanDetailModel>>(UiState.Idle)
    val detailPengajuanState: StateFlow<UiState<UserPengajuanDetailModel>> = _detailPengajuanState
    private val _downloadState = MutableStateFlow<UiState<Uri>>(UiState.Idle)
    val downloadState: StateFlow<UiState<Uri>> = _downloadState
    private val _downloadStateBalasan = MutableStateFlow<UiState<Uri>>(UiState.Idle)
    val downloadStateBalasan: StateFlow<UiState<Uri>> = _downloadStateBalasan
    private val _fileSelectedState = MutableStateFlow<UiState<FileSelectModel>>(UiState.Idle)
    val fileSelectedState: StateFlow<UiState<FileSelectModel>> = _fileSelectedState
    var pengajuanId = 0
    var fileSelectedModel: FileSelectModel? = null
    private val _updateDataState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val updateDataState: StateFlow<UiState<Unit>> = _updateDataState
    private val _destroyState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val destroyState: StateFlow<UiState<Unit>> = _destroyState
    var isApproveSelectedState = false
    var userPengajuanDetailModel: UserPengajuanDetailModel? = null
    var isSubmitReply = false

     fun getDetailPengajuan(id: Int) {
         viewModelScope.launch(Dispatchers.IO) {
             _detailPengajuanState.emit(UiState.Loading)
             try {
                 val response = getPengajuanDetailUseCase.invoke(id).getOrThrow()
                 userPengajuanDetailModel = response
                 _detailPengajuanState.emit(UiState.Success(response))
             }catch (e: Exception) {
                 e.printStackTrace()
                 Timber.e(e.message)
                 _detailPengajuanState.emit(UiState.Error(e.message ?: Constant.somethingWrong))
             }
         }
    }

    fun setIdleFileSelected() {
        _fileSelectedState.value = UiState.Idle
    }

    fun setIdleDownloadState() {
        _downloadStateBalasan.value = UiState.Idle
    }
    fun download(url: String, fileName: String, isFileBalasan: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFileBalasan) {
                _downloadStateBalasan.emit(UiState.Loading)
                val response = downloadFileUseCase(url, fileName)
                if (response.isSuccess) {
                    _downloadStateBalasan.emit(UiState.Success(response.getOrThrow()))
                } else {
                    _downloadStateBalasan.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
                }
            }else {
                _downloadState.emit(UiState.Loading)
                val response = downloadFileUseCase(url, fileName)
                if (response.isSuccess) {
                    _downloadState.emit(UiState.Success(response.getOrThrow()))
                } else {
                    _downloadState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
                }
            }

        }
    }

    fun validateFileSelected(uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val key = "balasan_file"
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

    fun validateUpdateData(balasanText: String?) {
        viewModelScope.launch {
            val aempId = getUserIdUseCase()
            val role = getRoleUseCase()
            val dataPengajuan = if (detailPengajuanState.value is UiState.Success) {
                (detailPengajuanState.value as UiState.Success).data
            }else {
                null
            }
            val pengajuId = dataPengajuan?.dataUser?.id ?: -1
            val pengajuanId = dataPengajuan?.dataPengajuan?.id ?: -1
            val validateResponse = validationReplyPengajuanUseCase(aempId, pengajuId, pengajuanId, role, fileSelectedModel, balasanText, isApproveSelectedState)
            if (validateResponse.isSuccess) {
                updateData(aempId, pengajuId, role, pengajuanId, balasanText)
            }else {
                _updateDataState.emit(UiState.Error(validateResponse.exceptionOrNull()?.message ?: Constant.somethingWrong))
            }
        }
    }

    fun setUpdateStateIdle() {
        _updateDataState.value  = UiState.Idle
    }

    fun updateData(aempId: Int, pengajuId: Int, role: String, pengajuanId: Int, balasanText: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateDataState.emit(UiState.Loading)
            val response = replyPengajuanUseCase(aempId, pengajuId, pengajuanId, role, fileSelectedModel, balasanText, isApproveSelectedState)
            if (response.isSuccess) {
                isSubmitReply = true
                _updateDataState.emit(UiState.Success(Unit))
            }else {
                _updateDataState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
            }
        }

    }

    fun destroyPengajuan() {
        viewModelScope.launch(Dispatchers.IO) {
            _destroyState.emit(UiState.Loading)
            val response = destroyPengajuanUseCase(pengajuanId)
            response
                .onSuccess { _destroyState.emit(UiState.Success(Unit)) }
                .onFailure { _destroyState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
        }
    }
}