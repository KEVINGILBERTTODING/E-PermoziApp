package com.example.e_permoziapp.di.repository

import com.example.e_permoziapp.data.common.repository.DownloadFileRepositoryImpl
import com.example.e_permoziapp.data.login.repository.LoginRepositoryImpl
import com.example.e_permoziapp.data.login.repository.SessionRepositoryImpl
import com.example.e_permoziapp.data.login.repository.UserRepositoryImpl
import com.example.e_permoziapp.data.pengajuan.remote.PengajuanServiceImpl
import com.example.e_permoziapp.data.pengajuan.repository.PengajuanRepositoryImpl
import com.example.e_permoziapp.data.register.repository.RegisterRepositoryImpl
import com.example.e_permoziapp.domain.repository.DownloadFileRepository
import com.example.e_permoziapp.domain.repository.LoginRepository
import com.example.e_permoziapp.domain.repository.PengajuanRepository
import com.example.e_permoziapp.domain.repository.RegisterRepository
import com.example.e_permoziapp.domain.repository.SessionRepository
import com.example.e_permoziapp.domain.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<RegisterRepository> { RegisterRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<SessionRepository> { SessionRepositoryImpl() }
    single<PengajuanRepository> { PengajuanRepositoryImpl(get()) }
    single<DownloadFileRepository> { DownloadFileRepositoryImpl(get(), get()) }
}