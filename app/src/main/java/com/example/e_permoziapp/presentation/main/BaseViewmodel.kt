package com.example.e_permoziapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.domain.usecase.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.LogOutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class BaseViewmodel(
    private val logOutUseCase: LogOutUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
): ViewModel() {
    private val _isLogOut = MutableSharedFlow<Boolean>()
    val isLogout: SharedFlow<Boolean> = _isLogOut

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getUserData()
        }
    }

    private suspend fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getUserDataUseCase.execute()
            val body = response.getOrNull()
            body?.let {
                val userStatus = body.status
                if (userStatus != "active") {
                    logOut()
                    _isLogOut.emit(true)
                }
            }
        }
    }

    fun logOut() {
        logOutUseCase.invoke()
    }
}