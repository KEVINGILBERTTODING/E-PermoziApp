package com.example.e_permoziapp.di.download

import com.example.e_permoziapp.core.util.DownloadHelper
import org.koin.dsl.module

val downloadModule = module {
    factory { DownloadHelper(get(), get())}
}