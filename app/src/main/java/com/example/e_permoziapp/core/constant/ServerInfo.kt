package com.example.e_permoziapp.core.constant

import com.example.e_permoziapp.BuildConfig

object ServerInfo {
    const val END_POINT = BuildConfig.BASE_URL
    const val BASE_URL = "${END_POINT}api/"
    const val FILE_PATH = "${END_POINT}perizinan/"
    const val IMAGE_PATH = "${END_POINT}data/img/profile/"
    const val FILE_PATH_PERSYARATAN = "${FILE_PATH}file_persyaratan/"
}