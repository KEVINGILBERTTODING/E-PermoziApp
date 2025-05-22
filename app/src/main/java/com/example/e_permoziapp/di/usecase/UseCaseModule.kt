package com.example.e_permoziapp.di.usecase

import com.example.e_permoziapp.domain.usecase.ClearAllUserInfoUseCase
import com.example.e_permoziapp.domain.usecase.GetIsLogginUseCase
import com.example.e_permoziapp.domain.usecase.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.LogOutUseCase
import com.example.e_permoziapp.domain.usecase.LoginUseCase
import com.example.e_permoziapp.domain.usecase.RegisterUseCase
import com.example.e_permoziapp.domain.usecase.SaveIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.SaveRoleUseCase
import com.example.e_permoziapp.domain.usecase.SaveUserIdUseCase
import com.example.e_permoziapp.domain.usecase.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.ValidateLoginUseCase
import com.example.e_permoziapp.domain.usecase.ValidateMobileNumberUseCase
import com.example.e_permoziapp.domain.usecase.ValidatePasswordUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { ValidatePasswordUseCase() }
    factory { ValidateEmailUseCase() }
    factory { ValidateFileUploadUseCase() }
    factory { RegisterUseCase(get()) }
    factory { ValidateFormTextUseCase() }
    factory { ValidateMobileNumberUseCase() }
    factory { GetUserIdUseCase(get()) }
    factory { GetIsLogginUseCase(get()) }
    factory { SaveUserIdUseCase(get()) }
    factory { SaveIsLoginUseCase(get()) }
    factory { ValidateLoginUseCase(get(), get()) }
    factory { ClearAllUserInfoUseCase(get()) }
    factory { LogOutUseCase(get()) }
    factory { SaveRoleUseCase(get()) }
}