package com.example.e_permoziapp.presentation.admin_employee.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.login.model.AEModel
import com.example.e_permoziapp.di.viewmodel.viewmodelModule
import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.profile.GetAdminEmployeeProfileUseCase
import com.example.e_permoziapp.domain.usecase.profile.ValidationAEUsecase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AeProfileViewmodel(
    private val getRoleUseCase: GetRoleUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getAdminEmployeeProfileUseCase: GetAdminEmployeeProfileUseCase,
    private val validationAEUsecase: ValidationAEUsecase
): ViewModel() {
    private var role = ""
    private var userId = 0
    var aeModel: AEModel? = null

    private val _profileState = MutableStateFlow<UiState<AEModel>>(UiState.Idle)
    val profileState: StateFlow<UiState<AEModel>> = _profileState


    init {
        role = getRoleUseCase()
        userId = getUserIdUseCase()
        getProfile()
    }

    fun getProfile() {
        _profileState.value = UiState.Loading
        val validateResponse = validationAEUsecase()
        viewModelScope.launch(Dispatchers.IO) {
            validateResponse
                .onFailure {
                    _profileState.emit(UiState.Error(validateResponse.exceptionOrNull()?.message ?: Constant.somethingWrong)) }
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val response = getAdminEmployeeProfileUseCase(userId, role).getOrThrow()
                    aeModel = response
                    _profileState.emit(UiState.Success(response))
                }catch (e: Exception) {
                    e.printStackTrace()
                    _profileState.emit(UiState.Error(e.message ?: Constant.somethingWrong))
                }

            }
        }
    }


}