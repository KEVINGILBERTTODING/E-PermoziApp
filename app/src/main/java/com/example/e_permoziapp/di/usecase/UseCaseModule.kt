package com.example.e_permoziapp.di.usecase

import com.example.e_permoziapp.domain.usecase.auth.ClearAllUserInfoUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserDataUseCase
import com.example.e_permoziapp.domain.usecase.auth.GetUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.LoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.LogoutUseCase
import com.example.e_permoziapp.domain.usecase.auth.RegisterUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveIsLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveRoleUseCase
import com.example.e_permoziapp.domain.usecase.auth.SaveUserIdUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateEmailUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFileUploadUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateFormTextUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateLoginUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidateMobileNumberUseCase
import com.example.e_permoziapp.domain.usecase.auth.ValidatePasswordUseCase
import com.example.e_permoziapp.domain.usecase.pengajuan.GetUserAllPengajuanUseCase
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
    factory { GetIsLoginUseCase(get()) }
    factory { SaveUserIdUseCase(get()) }
    factory { SaveIsLoginUseCase(get()) }
    factory { ValidateLoginUseCase(get(), get()) }
    factory { ClearAllUserInfoUseCase(get()) }
    factory { SaveRoleUseCase(get()) }
    factory { GetUserDataUseCase(get())}
    factory { GetUserAllPengajuanUseCase(get())}
    factory { LogoutUseCase(get(), get())}
}