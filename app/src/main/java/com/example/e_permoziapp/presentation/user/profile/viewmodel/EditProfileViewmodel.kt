package com.example.e_permoziapp.presentation.user.profile.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.core.util.ImageHelper
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.Entity.FileSelectModel
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.common.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.profile.UpdateUserProfileUseCase
import com.example.e_permoziapp.domain.usecase.profile.ValidateUpdateUserProfileUseCase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class EditProfileViewmodel(
    private val validateUpdateUserProfileUseCase: ValidateUpdateUserProfileUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase,
    private val context: Context
): ViewModel() {
    var userModel: UserModel? = null
    var fileSelectModel: FileSelectModel? = null
    private val _updateState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val updateState: StateFlow<UiState<Unit>> = _updateState
    private val _selectedFileState = MutableStateFlow<UiState<String?>>(UiState.Idle)
    val selectedFileState: StateFlow<UiState<String?>> = _selectedFileState

    fun validateForm(name: String?, email: String?,
                             password: String?, mobileNumber: String?){
        viewModelScope.launch {
            Timber.w("email", email)
            val userId = getUserIdUseCase()
            val validateResponse = validateUpdateUserProfileUseCase(name, email, password, mobileNumber, fileSelectModel)
            if (validateResponse.isFailure) {
                _updateState.emit(UiState.Error(validateResponse.exceptionOrNull()?.message ?: Constant.somethingWrong))
                return@launch
            }
            updateProfile(userId, name!!, email!!, password, mobileNumber!!)
        }
    }

    fun validateSelectedFile(uri: Uri) {
        viewModelScope.launch {
            try {
                val key = "ktp"
                val fileName = FileHelper.getFileNameFromUri(uri, context)
                val format = FileHelper.getMimeTypeFromUri(context, uri)
                val byteArray = ImageHelper.uriToBitmap(context, uri)

                val validateResponse = validateFileUploadUseCase(key, fileName, format, byteArray, true)
                validateResponse
                    .onSuccess {
                        fileSelectModel = FileSelectModel(uri, fileName, format, byteArray, key)
                        _selectedFileState.emit(UiState.Success(fileName))
                    }
                    .onFailure {
                        fileSelectModel = null
                        _selectedFileState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun updateProfile(userId: Int, name: String, email: String,
                              password: String?, mobileNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateState.emit(UiState.Loading)
            val response = updateUserProfileUseCase(userId, name, email, password, mobileNumber, fileSelectModel)
            response
                .onSuccess { _updateState.emit(UiState.Success(Unit)) }
                .onFailure { _updateState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong)) }
        }
    }
}