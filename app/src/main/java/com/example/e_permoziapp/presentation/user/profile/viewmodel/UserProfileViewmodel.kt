package com.example.e_permoziapp.presentation.user.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.pengajuan.model.UserProfilePengajuanModel
import com.example.e_permoziapp.domain.usecase.auth.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.UserProfilePengajuanUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidatePengajuanDate
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewmodel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val userProfilePengajuanUseCase: UserProfilePengajuanUseCase,
    private val validatePengajuanDate: ValidatePengajuanDate
): ViewModel() {
    private var userId = 0
    private val _userProfileState = MutableStateFlow<UiState<UserModel>>(UiState.Idle)
    val userProfileState: StateFlow<UiState<UserModel>> = _userProfileState
    private val _pengajuanState = MutableStateFlow<UiState<UserProfilePengajuanModel>>(UiState.Idle)
    val pengajuanState: StateFlow<UiState<UserProfilePengajuanModel>> = _pengajuanState

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

    private fun validatePengajuanDate(params: List<PengajuanModel>): List<PengajuanModel> {
        if (params.isNotEmpty()) {
            return params.map { it.copy(isEdit = validatePengajuanDate(it.createdAt, it.status)) }
        }
        return listOf()
    }
}