package com.example.e_permoziapp.di.viewmodel

import com.example.e_permoziapp.presentation.admin_employee.auth.viewmodel.LoginAEViewmodel
import com.example.e_permoziapp.presentation.admin_employee.home.viewmodel.HomeAEViewmodel
import com.example.e_permoziapp.presentation.admin_employee.pengajuan.viewmodel.DetailPengajuanAeViewmodel
import com.example.e_permoziapp.presentation.admin_employee.profile.viewmodel.AeEditProfileViewmodel
import com.example.e_permoziapp.presentation.admin_employee.profile.viewmodel.AeProfileViewmodel
import com.example.e_permoziapp.presentation.common.viewmodel.PhotoViewmodel
import com.example.e_permoziapp.presentation.user.login.viewmodel.LoginViewmodel
import com.example.e_permoziapp.presentation.main.viewmodel.BaseViewmodel
import com.example.e_permoziapp.presentation.manual_book.viewmodel.ManualBookViewmodel
import com.example.e_permoziapp.presentation.onboarding.viewmodel.OnBoardingViewmodel
import com.example.e_permoziapp.presentation.user.register.viewmodel.RegisterViewModel
import com.example.e_permoziapp.presentation.splash.ui.SplashViewmodel
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.DetailPengajuanViewmodel
import com.example.e_permoziapp.presentation.user.Pengajuan.viewmodel.SubmitPengajuanViewmodel
import com.example.e_permoziapp.presentation.user.home.viewmodel.HomeViewmodel
import com.example.e_permoziapp.presentation.user.profile.viewmodel.EditProfileViewmodel
import com.example.e_permoziapp.presentation.user.profile.viewmodel.UserProfileViewmodel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel { LoginViewmodel(get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SplashViewmodel(get(), get(), get()) }
    viewModel { BaseViewmodel(get(), get(), get(), get()) }
    viewModel { HomeViewmodel(get(), get(), get(), get(), get()) }
    viewModel { DetailPengajuanViewmodel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { DetailPengajuanAeViewmodel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SubmitPengajuanViewmodel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { UserProfileViewmodel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { EditProfileViewmodel(get(), get(), get(), get(),  get()) }
    viewModel { PhotoViewmodel() }
    viewModel { LoginAEViewmodel(get(), get(), get()) }
    viewModel { HomeAEViewmodel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AeProfileViewmodel(get(), get(), get(), get()) }
    viewModel { AeEditProfileViewmodel(get(), get(), get(), get(), get()) }
    viewModel { OnBoardingViewmodel(get(), get()) }
    viewModel { ManualBookViewmodel(get()) }
}