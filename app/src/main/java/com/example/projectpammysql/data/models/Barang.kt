package com.example.projectpammysql.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Barang(
    @SerialName("barang_id")
    val barangId: Int = 0,
    @SerialName("nama_barang")
    val namaBarang: String,
    @SerialName("stok")
    val stok: Int = 0,
    @SerialName("harga")
    val harga: Double,
    @SerialName("gambar_url")
    val gambarUrl: String? = null
)
@Serializable
data class BarangRequest(
    @SerialName("nama_barang")
    val namaBarang: String,
    @SerialName("stok")
    val stok: Int = 0,
    @SerialName("harga")
    val harga: Double,
    @SerialName("gambar_url")
    val gambarUrl: String? = null,
    @SerialName("gambar_base64")
    val gambarBase64: String? = null
)
@Serializable
data class BarangResponse(
    @SerialName("barang_id")
    val barangId: Int = 0,
    @SerialName("nama_barang")
    val namaBarang: String = "",
    @SerialName("stok")
    val stok: Int = 0,
    @SerialName("harga")
    val harga: String = "",
    @SerialName("gambar_url")
    val gambarUrl: String? = null
)
