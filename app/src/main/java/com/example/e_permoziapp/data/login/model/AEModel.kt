package com.example.e_permoziapp.data.login.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AEModel(
    @SerializedName("id")
    val id: Int? = 0,
    @SerialName("name")
    val name: String? = "",
    @SerialName("email")
    val email: String? = "",
    @SerialName("profile_photo")
    val profilePhoto: String? = "",
    @SerialName("role")
    val role: String? = ""
)