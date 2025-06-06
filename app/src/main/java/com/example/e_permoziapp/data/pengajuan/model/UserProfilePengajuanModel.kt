package com.example.e_permoziapp.data.pengajuan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfilePengajuanModel(
    @SerialName("data_proccess")
    var dataProccess: List<PengajuanModel>,
    @SerialName("data_success")
    var dataSuccess : List<PengajuanModel>,
    @SerialName("data_failed")
    var dataFailed: List<PengajuanModel>,
)