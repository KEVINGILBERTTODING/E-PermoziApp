package com.example.e_permoziapp.domain.usecase.auth

import com.example.e_permoziapp.core.util.PrefHelper

class ClearAllUserInfoUseCase(
    private val prefHelper: PrefHelper
) {
    operator fun invoke() {
        prefHelper.clear()
    }
}