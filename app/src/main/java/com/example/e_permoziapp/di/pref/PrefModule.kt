package com.example.e_permoziapp.di.pref

import com.example.e_permoziapp.core.util.PrefHelper
import org.koin.dsl.module

val prefModule = module {
    single { PrefHelper(get()) }
}