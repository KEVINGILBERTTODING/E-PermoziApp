package com.example.e_permoziapp.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.data.login.model.UserModel
import com.example.e_permoziapp.domain.usecase.LoginUseCase
import com.example.e_permoziapp.domain.usecase.SaveIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.SaveUserIdUseCase
import com.example.e_permoziapp.domain.usecase.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.ValidatePasswordUseCase
import com.example.e_permoziapp.presentation.common.UiState
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewmodel(
    private val loginUseCase: LoginUseCase,
    private val emailUseCase: ValidateEmailUseCase,
    private val passwordUseCase: ValidatePasswordUseCase,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val saveIsLoginUseCase: SaveIsLoginUseCase
) : ViewModel() {
    val _errorMessageState = MutableSharedFlow<String>()
    val errorMessageState : SharedFlow<String> = _errorMessageState
    val _loginState = MutableStateFlow<UiState<UserModel>>(UiState.Idle)
    val loginState: StateFlow<UiState<UserModel>> = _loginState
    private val TAG = "loginviewmodel"

    fun validateInput(email: String?, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultEmailVal = emailUseCase.invoke(email)
            val resultPasswordVal = passwordUseCase.invoke(password)
            if (resultEmailVal.isFailure){
                _errorMessageState.emit(resultEmailVal.exceptionOrNull()?.message ?: "Email error")
                return@launch
            }
            if (resultPasswordVal.isFailure){
                _errorMessageState.emit(resultPasswordVal.exceptionOrNull()?.message ?: "Password error")
                return@launch
            }
            login(email!!, password!!)
        }
    }

    suspend fun login(email: String, password: String) {
        _loginState.emit(UiState.Loading)
        try {
            val response = loginUseCase.login(email, password).getOrThrow()
            response?.let {
                saveUserIdUseCase.invoke(response.id)
                saveIsLoginUseCase.invoke(true)
            }
            _loginState.emit(UiState.Success(response))
        }catch (e: Exception) {
            _errorMessageState.emit(e.message.toString())
            _loginState.emit(UiState.Idle)
            Log.d(TAG, "login: ${e.message}")
        }
    }
}