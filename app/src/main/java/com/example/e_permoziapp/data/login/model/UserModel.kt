package com.example.e_permoziapp.data.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @SerialName("id")
    val id: Int,
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
)
