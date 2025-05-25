package com.example.e_permoziapp.data.persyaratan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersyaratanPerizinanModel (
    @SerialName("id")
    val id: Int,
    @SerialName("jenis_perizinan_id")
    val jenisPerizinanId: Int,
    @SerialName("name")
    val name: String = "",
    @SerialName("is_required")
    val isRequired: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String = "",
    @SerialName("content")
    val content: String? = null
)
