package com.example.e_permoziapp.data.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("email")
    val email: String = "",
    @SerialName("status")
    val status: String = "",
    @SerialName("profile_photo")
    val profilePhoto: String = "",
    @SerialName("fcm_token")
    val fcmToken: String? = "",
    @SerialName("mobile_number")
    val mobileNumber: String = "",
    @SerialName("ktp")
    val ktp: String? = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("role")
    val role: String? = "",
    @SerialName("nib")
    val nib: String? = "",
    @SerialName("npwp")
    val npwp: String? = "",
    @SerialName("nik")
    val nik: String? = "",
    @SerialName("address")
    val address: String? = "",
    @SerialName("tempat_lahir")
    val tempatLahir: String? = "",
    @SerialName("tgl_lahir")
    val tglLahir: String? = "",
    @SerialName("jenis_kelamin")
    val jenisKelamin: String? = "",
    @SerialName("agama")
    val agama: String? = "",
    @SerialName("pekerjaan")
    val pekerjaan: String? = "",
    @SerialName("kewarganegaraan")
    val kewarganegaraan: String? = "",
    @SerialName("is_verified")
    val isVerified: Boolean? = false
)
