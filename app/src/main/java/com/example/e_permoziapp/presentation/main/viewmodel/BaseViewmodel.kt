package com.example.e_permoziapp.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.domain.repository.SessionRepository
import com.example.e_permoziapp.domain.usecase.auth.ClearAllUserInfoUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import timber.log.Timber

class BaseViewmodel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val sessionRepository: SessionRepository
): ViewModel() {
    private val _isLogOut = MutableSharedFlow<Boolean>()
    private val isLogout: SharedFlow<Boolean> = _isLogOut
    private val logoutEvent: Flow<Unit> = sessionRepository.logOutEvent

    val logoutTrigger = merge(
        isLogout.filter { it },
        logoutEvent.map { true }
    ).distinctUntilChanged()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getUserData()
        }
    }

    private suspend fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = getUserIdUseCase.invoke()
            if (userId < 1) {
                logOut()
                return@launch
            }
            val response = getUserDataUseCase.execute(userId)
            Timber.d("response: $response")
            if (response.isSuccess) {
                val body = response.getOrNull()
                if (body != null) {
                    val userStatus = body.status
                    if (userStatus != "active") {
                        logOut()
                        _isLogOut.emit(true)
                    }
                }else {
                    return@launch
                }
            }
        }
    }

    fun logOut() {
       viewModelScope.launch {
           _isLogOut.emit(true)
       }
    }
}