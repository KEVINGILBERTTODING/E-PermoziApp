package com.example.e_permoziapp.di.context

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val contextModule =  module {
    single { androidContext() }
}