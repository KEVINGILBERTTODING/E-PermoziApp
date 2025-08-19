package com.example.e_permoziapp.data.pengajuan.model

import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePengajuanAEModel(
    @SerialName("start_date")
    val startDate: String? = "",
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("jenis_perizinan_id")
    val jenisPerizinanId: String? = null,
    @SerialName("jenis_perizinan")
    val jenisPerizinan: List<JenisPerizinanModel>? = listOf(),
    @SerialName("data_perizinan")
    val dataPerizinan: List<PengajuanModel>? = listOf()
)
