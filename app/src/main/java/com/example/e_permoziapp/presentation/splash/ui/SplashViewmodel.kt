package com.example.e_permoziapp.presentation.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.domain.usecase.auth.ValidateLoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SplashViewmodel(
    private val validateLoginUseCase: ValidateLoginUseCase
): ViewModel() {
    private val _isLoginValidate = MutableSharedFlow<Boolean>()
    val isLoginValidate: SharedFlow<Boolean> = _isLoginValidate

    init {
        viewModelScope.launch {
            delay(300)
            validateUser()
        }
    }

    private fun validateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUserValid = validateLoginUseCase.invoke()
            if (isUserValid) {
                _isLoginValidate.emit(true)
            }else {
                _isLoginValidate.emit(false)
            }
        }

    }
}