package com.example.e_permoziapp.di.network

import com.example.e_permoziapp.domain.remote.LoginService
import com.example.e_permoziapp.data.login.remote.LoginServiceImpl
import com.example.e_permoziapp.data.pengajuan.remote.PengajuanServiceImpl
import com.example.e_permoziapp.data.perizinan.remote.PerizinanServiceImpl
import com.example.e_permoziapp.domain.remote.RegisterService
import com.example.e_permoziapp.data.register.remote.RegisterServiceImpl
import com.example.e_permoziapp.data.user.remote.UserServiceImpl
import com.example.e_permoziapp.domain.remote.PengajuanService
import com.example.e_permoziapp.domain.remote.PerizinanService
import com.example.e_permoziapp.domain.remote.UserService
import com.example.e_permoziapp.domain.usecase.auth.LogoutUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.dsl.module

@OptIn(DelicateCoroutinesApi::class)
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
                        GlobalScope.launch(Dispatchers.Main) {
                            getKoin().get<LogoutUseCase>().invoke()
                        }
                    }
                }
            }
        }
    }

    single<LoginService> { LoginServiceImpl(get()) }
    single<RegisterService> { RegisterServiceImpl(get()) }
    single<UserService> { UserServiceImpl(get()) }
    single<PengajuanService> { PengajuanServiceImpl(get()) }
    single<PerizinanService> { PerizinanServiceImpl(get()) }
}