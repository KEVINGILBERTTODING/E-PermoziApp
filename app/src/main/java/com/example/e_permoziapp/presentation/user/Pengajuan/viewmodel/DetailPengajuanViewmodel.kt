package com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.pengajuan.model.UserPengajuanDetailModel
import com.example.e_permoziapp.domain.usecase.pengajuan.GetPengajuanDetailUseCase
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailPengajuanViewmodel(
    private val getPengajuanDetailUseCase: GetPengajuanDetailUseCase
): ViewModel() {
    private val _detailPengajuanState = MutableStateFlow<UiState<UserPengajuanDetailModel>>(UiState.Idle)
    val detailPengajuanState: StateFlow<UiState<UserPengajuanDetailModel>> = _detailPengajuanState

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
}