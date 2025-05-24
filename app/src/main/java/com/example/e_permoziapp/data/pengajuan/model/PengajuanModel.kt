package com.example.e_permoziapp.data.pengajuan.model

import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PengajuanModel (
    @SerialName("id")
    val id: Int,
    @SerialName("jenis_perizinan_id")
    val jenisPerizinanId: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("status")
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("jenis_perizinan")
    val jenisPerizinan: JenisPerizinanModel
)