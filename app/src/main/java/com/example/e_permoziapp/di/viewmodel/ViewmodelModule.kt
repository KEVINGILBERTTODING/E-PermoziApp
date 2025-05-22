package com.example.e_permoziapp.di.viewmodel

import com.example.e_permoziapp.presentation.login.LoginViewmodel
import com.example.e_permoziapp.presentation.main.BaseViewmodel
import com.example.e_permoziapp.presentation.register.RegisterViewModel
import com.example.e_permoziapp.presentation.splash.SplashViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel { LoginViewmodel(get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SplashViewmodel(get()) }
    viewModel { BaseViewmodel(get(), get()) }
}