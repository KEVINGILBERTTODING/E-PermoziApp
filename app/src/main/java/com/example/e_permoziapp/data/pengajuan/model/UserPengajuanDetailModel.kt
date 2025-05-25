package com.example.e_permoziapp.data.pengajuan.model

import com.example.e_permoziapp.data.balasan.model.BalasanModel
import com.example.e_permoziapp.data.perizinan.model.JenisPerizinanModel
import com.example.e_permoziapp.data.persyaratan.model.PersyaratanPerizinanModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPengajuanDetailModel (
    @SerialName("data_persyaratan")
    val dataPersyaratan: List<PersyaratanPerizinanModel>? = null,
    @SerialName("data_jenis_perizinan")
    val dataJenisPersyaratan: JenisPerizinanModel? = null,
    @SerialName("data_pengajuan")
    val dataPengajuan: PengajuanModel? = null,
    @SerialName("data_balasan")
    val dataBalasan: BalasanModel? = null
)