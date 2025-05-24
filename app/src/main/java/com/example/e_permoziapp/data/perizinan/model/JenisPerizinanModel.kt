package com.example.e_permoziapp.data.perizinan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JenisPerizinanModel (
    @SerialName("id")
    val id: Int,
    @SerialName("nama_perizinan")
    val namaPerizinan: String =  ""
)