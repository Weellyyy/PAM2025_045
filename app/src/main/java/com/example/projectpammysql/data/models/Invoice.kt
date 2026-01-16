package com.example.projectpammysql.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Invoice(
    @SerialName("invoice_id")
    val invoiceId: Int = 0,
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("file_url")
    val fileUrl: String? = null
)

@Serializable
data class InvoiceRequest(
    @SerialName("order_id")
    val orderId: Int
)

@Serializable
data class InvoiceResponse(
    @SerialName("invoice_id")
    val invoiceId: Int = 0,
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("file_url")
    val fileUrl: String? = null
)

@Serializable
data class InvoiceDetail(
    @SerialName("invoice_id")
    val invoiceId: Int = 0,
    @SerialName("order_id")
    val orderId: Int = 0,
    val tanggal: String = "",
    @SerialName("nama_toko")
    val namaToko: String = "",
    val username: String? = null,
    val total: String = "",
    val items: List<InvoiceItem> = emptyList()
)

@Serializable
data class InvoiceItem(
    @SerialName("nama_barang")
    val namaBarang: String,
    val jumlah: Int,
    @SerialName("harga_satuan")
    val hargaSatuan: Double
)
