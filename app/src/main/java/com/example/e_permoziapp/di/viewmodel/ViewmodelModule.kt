package com.example.e_permoziapp.di.viewmodel

import com.example.e_permoziapp.presentation.user.login.viewmodel.LoginViewmodel
import com.example.e_permoziapp.presentation.main.viewmodel.BaseViewmodel
import com.example.e_permoziapp.presentation.user.register.viewmodel.RegisterViewModel
import com.example.e_permoziapp.presentation.splash.ui.SplashViewmodel
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import com.example.e_permoziapp.presentation.user.home.viewmodel.HomeViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel { LoginViewmodel(get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SplashViewmodel(get()) }
    viewModel { BaseViewmodel(get(), get(), get()) }
    viewModel { HomeViewmodel(get(), get(), get()) }
    viewModel { DetailPengajuanViewmodel(get(), get(), get(), get(), get(), get()) }
}