package com.example.e_permoziapp.presentation.admin_employee.home.viewmodel

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.core.constant.ServerInfo
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.common.DownloadFileUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanAeUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.ValidationGetPengajuanAEUseCase
import com.example.e_permoziapp.domain.usecase.profile.GetAdminEmployeeProfileUseCase
import com.example.e_permoziapp.domain.usecase.profile.ValidationAEUsecase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeAEViewmodel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getRoleUseCase: GetRoleUseCase,
    private val validationGetPengajuanAEUseCase: ValidationGetPengajuanAEUseCase,
    private val getPengajuanAeUseCase: GetPengajuanAeUseCase,
    private val downloadFileUseCase: DownloadFileUseCase,
    private val getAdminEmployeeProfileUseCase: GetAdminEmployeeProfileUseCase,
    private val validationAEUsecase: ValidationAEUsecase
): ViewModel() {
    var startDate = ""
    var endDate = ""
    var userId = 0
    var role = ""
    var idJenisPerizinan = 0

    private val _getPengajuanState = MutableStateFlow<UiState<List<PengajuanModel>?>>(UiState.Idle)
    val getPengajuanState: StateFlow<UiState<List<PengajuanModel>?>> = _getPengajuanState


    private val _downloadState = MutableStateFlow<UiState<Uri>>(UiState.Idle)
    val downloadState : StateFlow<UiState<Uri>> = _downloadState

    private val _getUserState = MutableStateFlow<UiState<AEModel>>(UiState.Idle)
    val getUserState : StateFlow<UiState<AEModel>> = _getUserState

    private val _jenisPerizinanState = MutableStateFlow<MutableList<JenisPerizinanModel>?>(
        mutableListOf()
    )
    val jenisPerizinanState: StateFlow<MutableList<JenisPerizinanModel>?> = _jenisPerizinanState

    init {
        userId = getUserIdUseCase()
        role = getRoleUseCase()
        getProfile()
    }

    fun filterValidation() {
        _getPengajuanState.value = UiState.Loading
        val response = validationGetPengajuanAEUseCase(startDate, endDate, userId, role)
        response
            .onSuccess { getPengajuan() }
            .onFailure { _getPengajuanState.value = UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong) }
    }

    private fun getPengajuan() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getPengajuanAeUseCase(startDate, endDate, userId, role, idJenisPerizinan).getOrThrow()
                response.jenisPerizinan?.let {
                    _jenisPerizinanState.emit(it.toMutableList())
                }
                response.startDate?.let { startDate = it }
                response.endDate?.let { endDate = it }
                _getPengajuanState.emit(UiState.Success(response.dataPerizinan))
            }catch (e: Exception) {
                _getPengajuanState.emit(UiState.Error(e.message ?: Constant.somethingWrong))
            }
        }
    }

    fun downloadReport() {
        val url = generateDownloadUrl()
        val fileName = generateFileName()
        _downloadState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = downloadFileUseCase(url, fileName)
            if (response.isSuccess) {
                _downloadState.emit(UiState.Success(response.getOrNull()!!))
            } else {
                _downloadState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
            }
        }
    }

    private fun generateFileName(): String {
        return "Laporan_${startDate}_${endDate}.pdf"
    }

    private fun generateDownloadUrl(): String {
        return  "${ServerInfo.BASE_URL}admin-employee/pengajuan/filter/download?start_date=$startDate&end_date=$endDate&jenis_perizinan_id=$idJenisPerizinan"
    }

    fun getProfile() {
        _getUserState.value = UiState.Loading
        val userValidation = validationAEUsecase()
        userValidation
            .onFailure {
                _getUserState.value = UiState.Error(userValidation.exceptionOrNull()?.message ?: Constant.somethingWrong)
                return
            }
            .onSuccess {
                viewModelScope.launch(Dispatchers.IO) {
                    getAdminEmployeeProfileUseCase(userId, role)
                        .onSuccess { _getUserState.emit(UiState.Success(it)) }
                        .onFailure { _getUserState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
                }
            }

    }



}