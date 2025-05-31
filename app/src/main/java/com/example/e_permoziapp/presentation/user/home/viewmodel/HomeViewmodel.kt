package com.example.e_permoziapp.presentation.user.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.pengajuan.model.PengajuanModel
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.domain.repository.PerizinanRepository
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanByUserIdUseCase
import com.example.e_permoziapp.domain.usecase.perizinan.GetJenisPeriziananUseCase
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewmodel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val pengajuanByUserIdUseCase: GetPengajuanByUserIdUseCase,
    private val getJenisPeriziananUseCase: GetJenisPeriziananUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<PengajuanModel>?>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<PengajuanModel>?>> = _uiState

    private val _jenisPerizinanUiState = MutableStateFlow<UiState<List<JenisPerizinanModel>>>(UiState.Idle)
    val jenisPerizinanUiState: StateFlow<UiState<List<JenisPerizinanModel>>> = _jenisPerizinanUiState

    fun getAllPengajuan() {
        viewModelScope.launch(Dispatchers.IO){
            _uiState.emit(UiState.Loading)
            try {
                val userId = getUserIdUseCase.invoke()
                val response = pengajuanByUserIdUseCase.invoke(userId)
                val dataPengajuan = response.getOrNull()
                if (response.isSuccess) {
                    _uiState.emit(UiState.Success(dataPengajuan))
                }else {
                    _uiState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
                }
            }catch (e: Exception) {
                _uiState.emit(UiState.Error(e.message.toString()))
            }


        }

    }

    fun getJenisPerizinan() {
        viewModelScope.launch(Dispatchers.IO){
            _jenisPerizinanUiState.emit(UiState.Loading)
            try {
                val response = getJenisPeriziananUseCase()
                val dataPerizinan = response.getOrNull()
                if (response.isSuccess && !dataPerizinan.isNullOrEmpty()) {
                    _jenisPerizinanUiState.emit(UiState.Success(dataPerizinan))
                }else {
                    _jenisPerizinanUiState.emit(UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong))
                }
            }catch (e: Exception) {
                _jenisPerizinanUiState.emit(UiState.Error(e.message.toString()))
            }
        }
    }
}