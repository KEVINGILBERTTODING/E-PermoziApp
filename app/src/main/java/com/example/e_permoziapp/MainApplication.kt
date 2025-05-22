package com.example.e_permoziapp

import android.app.Application
import com.example.e_permoziapp.di.context.contextModule
import com.example.e_permoziapp.di.network.networkModule
import com.example.e_permoziapp.di.pref.prefModule
import com.example.e_permoziapp.di.repository.repositoryModule
import com.example.e_permoziapp.di.usecase.useCaseModule
import com.example.e_permoziapp.di.viewmodel.viewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            Timber.plant(Timber.DebugTree())
            modules(listOf(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewmodelModule,
                prefModule)
            )
        }
    }
}