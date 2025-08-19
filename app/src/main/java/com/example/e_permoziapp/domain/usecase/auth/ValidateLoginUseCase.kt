package com.example.e_permoziapp.domain.usecase.auth

import timber.log.Timber

class ValidateLoginUseCase(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getIsLoginUseCase: GetIsLoginUseCase,
    private val getRoleUseCase: GetRoleUseCase
) {
    operator fun invoke(): Boolean {
        Timber.w("user_id ${getUserIdUseCase.invoke()}")
        return getUserIdUseCase() > 0 && getIsLoginUseCase() && getRoleUseCase().isEmpty().not()
    }
}