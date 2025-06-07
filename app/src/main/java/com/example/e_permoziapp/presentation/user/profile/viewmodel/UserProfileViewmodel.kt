package com.example.e_permoziapp.presentation.user.profile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.core.util.ImageHelper
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.UserProfilePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidatePengajuanDate
import com.example.e_permoziapp.domain.usecase.profile.UpdateUserPhotoUseCase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewmodel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val userProfilePengajuanUseCase: UserProfilePengajuanUseCase,
    private val validatePengajuanDate: ValidatePengajuanDate,
    private val context: Context,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase
): ViewModel() {
    private var userId = 0
    var userModel: UserModel? = null
    var fileSelectModel: FileSelectModel? = null
    private val _userProfileState = MutableStateFlow<UiState<UserModel>>(UiState.Idle)
    val userProfileState: StateFlow<UiState<UserModel>> = _userProfileState
    private val _pengajuanState = MutableStateFlow<UiState<UserProfilePengajuanModel>>(UiState.Idle)
    val pengajuanState: StateFlow<UiState<UserProfilePengajuanModel>> = _pengajuanState
    private val _updatePhotoState = MutableStateFlow<UiState<Uri?>>(UiState.Idle)
    val updatePhotoState: StateFlow<UiState<Uri?>> = _updatePhotoState

    init {
        userId = getUserIdUseCase()
    }

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _userProfileState.emit(UiState.Loading)
            val response = getUserDataUseCase.execute(userId)
            val body = response.getOrNull()
            response
                .onSuccess {
                    if(body != null) {
                        userModel = body
                        _userProfileState.emit(UiState.Success(body))
                    }else {
                        _userProfileState.emit(UiState.Error("Gagal mendapatkan data user"))
                    }
                }
                .onFailure { _userProfileState.emit(UiState.Error(it.message.toString())) }
        }
    }

    fun getPengajuan() {
        viewModelScope.launch(Dispatchers.IO) {
            _pengajuanState.emit(UiState.Loading)
            val response = userProfilePengajuanUseCase(userId)
            val body = response.getOrNull()
            response
                .onSuccess {
                    val data = UserProfilePengajuanModel
                    data.apply {
                        it.dataSuccess = validatePengajuanDate(it.dataSuccess)
                        it.dataProccess= validatePengajuanDate(it.dataProccess)
                        it.dataFailed = validatePengajuanDate(it.dataFailed)
                    }
                    _pengajuanState.emit(UiState.Success(body!!))
                }
                .onFailure {
                    _pengajuanState.emit(UiState.Error(it.message.toString()))
                }
        }
    }

    fun validateSelectedFile(uri: Uri) {
        viewModelScope.launch {
            try {
                val key = "profile_photo"
                val fileName = FileHelper.getFileNameFromUri(uri, context)
                val format = FileHelper.getMimeTypeFromUri(context, uri)
                val byteArray = ImageHelper.uriToBitmap(context, uri)

                val validateResponse = validateFileUploadUseCase(key, fileName, format, byteArray, true)
                validateResponse
                    .onSuccess {
                        fileSelectModel = FileSelectModel(uri, fileName, format, byteArray, key)
                        updatePhotoProfile()
                    }
                    .onFailure {
                        fileSelectModel = null
                        _updatePhotoState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updatePhotoProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _userProfileState.emit(UiState.Loading)
            val userId = getUserIdUseCase()
            val response = updateUserPhotoUseCase(userId, fileSelectModel!!)
            response
                .onSuccess {
                    _updatePhotoState.emit(UiState.Success(fileSelectModel!!.uri))
                }
                .onFailure {
                    _updatePhotoState.emit(UiState.Error(it.message.toString()))
                }
        }
    }

    private fun validatePengajuanDate(params: List<PengajuanModel>): List<PengajuanModel> {
        if (params.isNotEmpty()) {
            return params.map { it.copy(isEdit = validatePengajuanDate(it.createdAt, it.status)) }
        }
        return listOf()
    }
}