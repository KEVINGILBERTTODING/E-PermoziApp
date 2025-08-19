package com.example.e_permoziapp.data.balasan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BalasanModel(
    @SerialName("admin_id")
    val adminId: Int,
    @SerialName("balasan_text")
    val balasanText: String? = "",
    @SerialName("created_at")
    val createdAt: String? = "",
    @SerialName("employee_id")
    val employeeId: Int?,
    @SerialName("file")
    val file: String? = "",
    @SerialName("id")
    val id: Int,
    @SerialName("pengajuan_perizinan_id")
    val pengajuanPerizinanId: Int,
    @SerialName("updated_at")
    val updatedAt: String? = ""
)