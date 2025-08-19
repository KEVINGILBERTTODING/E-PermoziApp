package com.example.e_permoziapp.presentation.admin_employee.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.domain.usecase.auth.LoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.LoginValidationUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveUserInfoUseCase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginAEViewmodel(
    private val loginAEvalidationUseCase: LoginValidationUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState

    fun formValidation(email: String?, password: String?, role: String) {
        _loginState.value = UiState.Loading
        val response = loginAEvalidationUseCase(email, password)
        response
            .onSuccess { login(email!!, password!!, role) }
            .onFailure { _loginState.value = UiState.Error(response.exceptionOrNull()?.message ?: Constant.somethingWrong) }

    }

    fun login(email: String, password: String, role: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginUseCase.loginAe(email, password, role).getOrThrow()
                val data = response
                val saveUserInfo = saveUserInfoUseCase(data.id ?: 0, true, data.role ?: "")
                saveUserInfo
                    .onSuccess { _loginState.emit(UiState.Success(Unit)) }
                    .onFailure { _loginState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
            }catch (e: Exception) {
                e.printStackTrace()
                _loginState.emit(UiState.Error(e.message ?: Constant.somethingWrong))
            }
        }
    }
}