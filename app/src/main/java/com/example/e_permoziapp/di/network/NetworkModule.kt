package com.example.e_permoziapp.di.network

import com.example.e_permoziapp.domain.remote.LoginService
import com.example.e_permoziapp.data.login.remote.LoginServiceImpl
import com.example.e_permoziapp.data.main.remote.BaseServiceImpl
import com.example.e_permoziapp.domain.remote.RegisterService
import com.example.e_permoziapp.data.register.remote.RegisterServiceImpl
import com.example.e_permoziapp.domain.remote.BaseService
import com.example.e_permoziapp.presentation.main.BaseActivity
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
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
            expectSuccess = false

            HttpResponseValidator {
                validateResponse {
                    if (it.status == HttpStatusCode.Unauthorized) {
                        BaseActivity().logOut()
                    }
                }
            }
        }
    }

    single<LoginService> { LoginServiceImpl(get()) }
    single<RegisterService> { RegisterServiceImpl(get()) }
    single<BaseService> { BaseServiceImpl(get()) }
}