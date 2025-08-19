package com.example.e_permoziapp.presentation.user.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.core.constant.Constant
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.usecase.auth.LoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.LoginValidationUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveUserInfoUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.presentation.common.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewmodel(
    private val loginUseCase: LoginUseCase,
    private val loginValidationUseCase: LoginValidationUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase
) : ViewModel() {
    val _loginState = MutableStateFlow<UiState<UserModel>>(UiState.Idle)
    val loginState: StateFlow<UiState<UserModel>> = _loginState

    fun validateInput(email: String?, password: String?) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            val response = loginValidationUseCase(email, password)
            response
                .onSuccess {
                    login(email!!, password!!)
                }
                .onFailure { _loginState.emit(UiState.Error(it.message ?: Constant.somethingWrong)) }
        }
    }

    fun login(email: String, password: String) {
       viewModelScope.launch(Dispatchers.IO) {
           try {
               val response = loginUseCase.login(email, password).getOrThrow()
               saveUserInfoUseCase(response.id, true, response.role!!).getOrThrow()
               _loginState.emit(UiState.Success(response))
           }catch (e: Exception) {
               Timber.d("response login ${e.printStackTrace()}")
               _loginState.emit(UiState.Error(e.message ?: ""))
           }
       }
    }
}