package com.example.e_permoziapp.presentation.user.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.usecase.auth.LoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginViewmodel(
    private val loginUseCase: LoginUseCase,
    private val emailUseCase: ValidateEmailUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val saveIsLoginUseCase: SaveIsLoginUseCase
) : ViewModel() {
    val _loginState = MutableStateFlow<UiState<UserModel>>(UiState.Idle)
    val loginState: StateFlow<UiState<UserModel>> = _loginState

    fun validateInput(email: String?, password: String?) {
        viewModelScope.launch {
            val resultEmailVal = emailUseCase.invoke(email)
            val resultPasswordVal = passwordUseCase.invoke(password)
            if (resultEmailVal.isFailure){
                _loginState.emit(UiState.Error(resultEmailVal.exceptionOrNull()?.message ?: "Email error"))
                return@launch
            }
            if (resultPasswordVal.isFailure){
                _loginState.emit(UiState.Error(resultPasswordVal.exceptionOrNull()?.message ?: "Password error"))
                return@launch
            }
            login(email!!, password!!)
        }
    }

    suspend fun login(email: String, password: String) {
       viewModelScope.launch(Dispatchers.IO) {
           _loginState.emit(UiState.Loading)
           try {
               val response = loginUseCase.login(email, password).getOrThrow()

               saveUserIdUseCase.invoke(response.id)
               saveIsLoginUseCase.invoke(true)
               _loginState.emit(UiState.Success(response))
           }catch (e: Exception) {
               Timber.d("response login ${e.printStackTrace()}")
               _loginState.emit(UiState.Error(e.message ?: ""))
           }
       }
    }
}