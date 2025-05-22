package com.example.e_permoziapp.data.login.repository

import com.example.e_permoziapp.core.util.PrefHelper
import com.example.e_permoziapp.domain.repository.UserRepository

class UserRepositoryImpl(
    private val prefHelper: PrefHelper
): UserRepository {
    private val userIdKey = "user_id"
    private val isLoggedKey = "is_logged"
    private val roleKey = "role"

    override fun saveUserId(userId: Int) {
        prefHelper.putInt(userIdKey, userId)
    }

    override fun getUserId(): Int {
        return prefHelper.getInt(userIdKey)
    }

    override fun saveIsLogged(logged: Boolean) {
        prefHelper.putBoolen(isLoggedKey, logged)
    }

    override fun getIsLogged(): Boolean {
        return prefHelper.getBoolen(isLoggedKey)
    }

    override fun saveRole(role: String) {
        prefHelper.putString(roleKey, role)
    }
}