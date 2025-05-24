package com.example.e_permoziapp.data.login.repository

import com.example.e_permoziapp.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class SessionRepositoryImpl: SessionRepository {
    val _isLogout = MutableSharedFlow<Unit>()

    override val logOutEvent: Flow<Unit>
        get() = _isLogout

    override suspend fun logOut() {
        _isLogout.emit(Unit)
    }


}