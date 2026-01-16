package com.example.projectpammysql.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ===== ORDER DETAIL RESPONSE (untuk menampilkan detail order dengan items) =====
@Serializable
data class OrderDetail(
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("toko_id")
    val tokoId: Int = 0,
    @SerialName("user_id")
    val userId: Int? = null,
    val total: String = "",
    val status: String = "",
    @SerialName("nama_toko")
    val namaToko: String = "",
    val username: String? = null,
    val items: List<OrderItemDetail> = emptyList()
)

@Serializable
data class OrderItemDetail(
    @SerialName("barang_id")
    val barangId: Int,
    @SerialName("nama_barang")
    val namaBarang: String,
    val jumlah: Int,
    @SerialName("harga_satuan")
    val hargaSatuan: Double,
    @SerialName("gambar_url")
    val gambarUrl: String? = null
)

// ===== ORDER DETAIL TABLE (untuk tabel order_detail di database) =====
@Serializable
data class OrderDetailItem(
    @SerialName("orderdetail_id")
    val orderDetailId: Int = 0,
    @SerialName("order_id")
    val orderId: Int = 0,
    @SerialName("barang_id")
    val barangId: Int = 0,
    val jumlah: Int = 0,
    @SerialName("harga_satuan")
    val hargaSatuan: String = "0",
    val subtotal: String = "0"
)

@Serializable
data class OrderDetailItemRequest(
    @SerialName("barang_id")
    val barangId: Int,
    val jumlah: Int,
    @SerialName("harga_satuan")
    val hargaSatuan: Double,
    val subtotal: Double
)

@Serializable
data class OrderDetailItemResponse(
    @SerialName("orderdetail_id")
    val orderDetailId: Int = 0,
    @SerialName("order_id")
    val orderId: Int = 0,
    @SerialName("barang_id")
    val barangId: Int = 0,
    @SerialName("nama_barang")
    val namaBarang: String = "",
    val jumlah: Int = 0,
    @SerialName("harga_satuan")
    val hargaSatuan: String = "0",
    val subtotal: String = "0"
)
