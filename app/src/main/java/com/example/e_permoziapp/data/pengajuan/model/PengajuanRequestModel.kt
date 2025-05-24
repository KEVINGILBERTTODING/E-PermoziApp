package com.example.e_permoziapp.data.pengajuan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PengajuanRequestModel (
    @SerialName("user_id")
    val userId: Int
)