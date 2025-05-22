package com.example.e_permoziapp.di.network

import com.example.e_permoziapp.data.login.remote.LoginService
import com.example.e_permoziapp.data.login.remote.LoginServiceImpl
import com.example.e_permoziapp.data.register.remote.RegisterService
import com.example.e_permoziapp.data.register.remote.RegisterServiceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single<LoginService> { LoginServiceImpl(get()) }
    single<RegisterService> { RegisterServiceImpl(get()) }
}