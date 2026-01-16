package com.example.projectpammysql.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Toko(
    @SerialName("toko_id")
    val tokoId: Int = 0,
    @SerialName("nama_toko")
    val namaToko: String,
    val alamat: String? = null,
    val kontak: String? = null
)

@Serializable
data class TokoRequest(
    @SerialName("nama_toko")
    val namaToko: String,
    val alamat: String? = null,
    val kontak: String? = null
)

@Serializable
data class TokoResponse(
    @SerialName("toko_id")
    val tokoId: Int = 0,
    @SerialName("nama_toko")
    val namaToko: String = "",
    val alamat: String? = null,
    val kontak: String? = null
)
