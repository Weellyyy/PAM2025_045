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
    val tokoId: Int? = null,  // ✅ Ubah jadi nullable
    @SerialName("user_id")
    val userId: Int? = null,  // ✅ Sudah nullable
    val total: String = "",
    val status: String = "",
    @SerialName("nama_toko")
    val namaToko: String? = null,  // ✅ Ubah jadi nullable
    val username: String? = null,  // ✅ Sudah nullable
    val items: List<OrderItemDetail> = emptyList()
)

@Serializable
data class OrderItemDetail(
    @SerialName("barang_id")
    val barangId: Int? = null,  // ✅ Ubah jadi nullable (barang bisa dihapus)
    @SerialName("nama_barang")
    val namaBarang: String? = null,  // ✅ Ubah jadi nullable
    val jumlah: Int,
    @SerialName("harga_satuan")
    val hargaSatuan: Double,
    @SerialName("gambar_url")
    val gambarUrl: String? = null  // ✅ Sudah nullable
)

// ===== ORDER DETAIL TABLE (untuk tabel order_detail di database) =====
@Serializable
data class OrderDetailItem(
    @SerialName("orderdetail_id")
    val orderDetailId: Int = 0,
    @SerialName("order_id")
    val orderId: Int = 0,
    @SerialName("barang_id")
    val barangId: Int? = null,  // ✅ Ubah jadi nullable
    val jumlah: Int = 0,
    @SerialName("harga_satuan")
    val hargaSatuan: String = "0",
    val subtotal: String = "0"
)

@Serializable
data class OrderDetailItemRequest(
    @SerialName("barang_id")
    val barangId: Int? = null,  // ✅ Ubah jadi nullable
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
    val barangId: Int? = null,  // ✅ Ubah jadi nullable
    @SerialName("nama_barang")
    val namaBarang: String? = null,  // ✅ Ubah jadi nullable
    val jumlah: Int = 0,
    @SerialName("harga_satuan")
    val hargaSatuan: String = "0",
    val subtotal: String = "0"
)