package com.example.e_permoziapp.di.repository

import com.example.e_permoziapp.data.login.repository.LoginRepositoryImpl
import com.example.e_permoziapp.data.login.repository.UserRepositoryImpl
import com.example.e_permoziapp.data.main.repository.BaseRepositoryImpl
import com.example.e_permoziapp.data.register.repository.RegisterRepositoryImpl
import com.example.e_permoziapp.domain.repository.BaseRepository
import com.example.e_permoziapp.domain.repository.LoginRepository
import com.example.e_permoziapp.domain.repository.RegisterRepository
import com.example.e_permoziapp.domain.repository.UserRepository
import org.koin.core.scope.get
import org.koin.dsl.module

val repositoryModule = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<RegisterRepository> { RegisterRepositoryImpl(get()) }
    single<UserRepository>  { UserRepositoryImpl(get()) }
    single<BaseRepository>  { BaseRepositoryImpl(get()) }
}