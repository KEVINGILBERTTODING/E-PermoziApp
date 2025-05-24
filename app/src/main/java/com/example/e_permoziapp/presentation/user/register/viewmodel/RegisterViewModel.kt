package com.example.e_permoziapp.presentation.user.register.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.util.FileHelper
import com.example.e_permoziapp.core.util.ImageHelper
import com.example.e_permoziapp.domain.Entity.ImageSelectModel
import com.example.e_permoziapp.domain.usecase.auth.RegisterUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateMobileNumberUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val application: Application,
    private val registerUseCase: RegisterUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
    private val emailUseCase: ValidateEmailUseCase,
    private val context: Context,
    private val validateFormTextUseCase: ValidateFormTextUseCase,
    private val validateMobileNumberUseCase: ValidateMobileNumberUseCase,
    private val validateFileUploadUseCase: ValidateFileUploadUseCase
) : ViewModel() {
    val _imgSelected = MutableStateFlow(ImageSelectModel(null,""))
    val imgSelected: StateFlow<ImageSelectModel> = _imgSelected
    val _uiState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val uiState : StateFlow<UiState<String>> = _uiState

    fun getFilename(uri: Uri) {
        val fileName = FileHelper.getFileNameFromUri(uri, application)
        _imgSelected.value = ImageSelectModel(uri, fileName)
    }

    fun validateRegiserForm(email: String?, name: String?, password: String?, mobileNumber: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val validateEmail = emailUseCase.invoke(email)
            val validatePassword = passwordUseCase.invoke(password)
            val validateFullname = validateFormTextUseCase.invoke(name)
            val validateMobileNumber = validateMobileNumberUseCase.invoke(mobileNumber)
            val uriKtp = imgSelected.value.uri
            val ktpBteArray = ImageHelper.uriToBitmap(context, uriKtp)
            val fileFormat = FileHelper.getMimeTypeFromUri(context, uriKtp)
            val fileUploadUseCase = validateFileUploadUseCase.invoke("ktp", imgSelected.value.fileName, fileFormat, ktpBteArray)

            if (validateEmail.isFailure) {
                _uiState.emit(UiState.Error(validateEmail.exceptionOrNull()?.message ?: "Email tidak valid"))
                return@launch
            }
            if (validateFullname.not()) {
                _uiState.emit(UiState.Error("Nama tidak boleh kosong"))
                return@launch
            }
            if (validatePassword.isFailure) {
                _uiState.emit(UiState.Error(validatePassword.exceptionOrNull()?.message ?: "Password tidak valid"))
                return@launch
            }
            if (validateMobileNumber.isFailure) {
                _uiState.emit(UiState.Error(validateMobileNumber.exceptionOrNull()?.message ?: "Nomor telepon tidak valid"))
                return@launch
            }

            if (fileUploadUseCase.isFailure) {
                _uiState.emit(UiState.Error(fileUploadUseCase.exceptionOrNull()?.message ?: "File tidak valid"))
                return@launch
            }
            register(email!!, name!!, password!!, mobileNumber!!, ktpBteArray!!)
        }
    }

    suspend fun register(email: String, name: String, password: String, mobileNumber: String, ktp: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(UiState.Loading)
            val response = registerUseCase.invoke(email, name, password, mobileNumber, ktp)
            if (response.isSuccess) {
                _uiState.emit(UiState.Success("Berhasil registrasi"))
            } else {
                _uiState.emit(UiState.Error(response.exceptionOrNull()?.message ?: "Terjadi kesalahan"))
            }

        }
    }

}