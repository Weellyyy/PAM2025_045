package com.example.projectpammysql.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("toko_id")
    val tokoId: Int? = 0,  // ✅ Nullable
    @SerialName("user_id")
    val userId: Int? = 0,  // ✅ Nullable
    val total: String = "0",
    val status: String = "pending"
)

@Serializable
data class OrderItem(
    @SerialName("barang_id")
    val barangId: Int? = 0,  // ✅ Nullable (jika barang dihapus)
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
    val tokoId: Int? = null,  // ✅ Sudah nullable
    @SerialName("user_id")
    val userId: Int? = null,  // ✅ Sudah nullable
    val total: String = "0",
    val status: String = "pending",
    @SerialName("nama_toko")
    val namaToko: String? = null,  // ✅ Ubah jadi nullable
    val username: String? = null,  // ✅ Sudah nullable
    @SerialName("invoice_id")
    val invoiceId: Int? = null,  // ✅ Sudah nullable
    @SerialName("file_url")
    val fileUrl: String? = null,  // ✅ Sudah nullable
    val items: List<OrderItem> = emptyList()
)