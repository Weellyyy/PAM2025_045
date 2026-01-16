package com.example.projectpammysql.data.models

data class OrderItemForm(
    val barangId: Int = 0,
    val namaBarang: String = "",
    val jumlah: Int = 1,
    val hargaSatuan: Double = 0.0
) {
    fun toOrderItem() = OrderItem(
        barangId = barangId,
        jumlah = jumlah,
        hargaSatuan = hargaSatuan
    )

    fun getSubtotal() = jumlah * hargaSatuan
}

