package com.example.projectpammysql.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("toko_id")
    val tokoId: Int = 0,
    @SerialName("user_id")
    val userId: Int = 0,
    val total: String = "0",
    val status: String = "pending"
)

@Serializable
data class OrderItem(
    @SerialName("barang_id")
    val barangId: Int,
    val jumlah: Int,
    @SerialName("harga_satuan")
    val hargaSatuan: Double
)

@Serializable
data class OrderRequest(
    @SerialName("toko_id")
    val tokoId: Int,
    @SerialName("user_id")
    val userId: Int,
    val status: String = "pending",
    val items: List<OrderItem> = emptyList()
)

@Serializable
data class OrderResponse(
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("toko_id")
    val tokoId: Int = 0,
    @SerialName("user_id")
    val userId: Int? = null,
    val total: String = "0",
    val status: String = "pending",
    @SerialName("nama_toko")
    val namaToko: String = "",
    val username: String? = null,
    @SerialName("invoice_id")
    val invoiceId: Int? = null,
    @SerialName("file_url")
    val fileUrl: String? = null,
    val items: List<OrderItem> = emptyList()
)

