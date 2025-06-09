package com.example.e_permoziapp.presentation.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateLoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SplashViewmodel(
    private val validateLoginUseCase: ValidateLoginUseCase,
    private val getRoleUseCase: GetRoleUseCase
): ViewModel() {
    private val _isLoginValidate = MutableSharedFlow<Boolean>()
    val isLoginValidate: SharedFlow<Boolean> = _isLoginValidate
    var role: String = ""

    init {
        viewModelScope.launch {
            delay(3000)
            validateUser()
        }
    }

    private fun validateUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val isUserValid = validateLoginUseCase.invoke()
            role = getRoleUseCase()
            if (isUserValid) {
                _isLoginValidate.emit(true)
            }else {
                _isLoginValidate.emit(false)
            }
        }

    }
}