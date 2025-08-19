package com.example.e_permoziapp.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_permoziapp.domain.repository.SessionRepository
import com.example.e_permoziapp.domain.usecase.auth.ClearAllUserInfoUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.LogoutUseCase
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
import kotlin.math.log

class BaseViewmodel(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val sessionRepository: SessionRepository,
    private val logoutUseCase: LogoutUseCase,
    private val getRoleUseCase: GetRoleUseCase
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
            validateUserId()
        }
    }

    private fun validateUserId() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = getUserIdUseCase.invoke()
            val role = getRoleUseCase()
            if (userId < 1) {
                logOut()
                return@launch
            }
            if (role.isEmpty()) {
                logOut()
                return@launch
            }
        }
    }

    fun logOut() {
       viewModelScope.launch {
           logoutUseCase.invoke()
           _isLogOut.emit(true)
       }
    }
}