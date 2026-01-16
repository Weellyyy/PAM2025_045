package com.example.projectpammysql.utils

import com.example.projectpammysql.data.models.BarangResponse
import com.example.projectpammysql.data.models.OrderItemForm


object StokValidator {

    fun validateStok(
        items: List<OrderItemForm>,
        barangList: List<BarangResponse>
    ): String? {
        for (item in items) {
            // Cari barang di list
            val barang = barangList.find { it.barangId == item.barangId }

            if (barang == null) {
                return "Barang dengan ID ${item.barangId} tidak ditemukan"
            }

            // Cek stok cukup
            val stokTersedia = barang.stok
            if (stokTersedia < item.jumlah) {
                return "Stok ${barang.namaBarang} tidak cukup. Tersedia: $stokTersedia, Diminta: ${item.jumlah}"
            }
        }

        return null  // Semua valid
    }

    fun validateSingleItem(
        barang: BarangResponse,
        jumlahDiminta: Int
    ): String? {
        val stokTersedia = barang.stok

        return when {
            stokTersedia == 0 -> "Stok ${barang.namaBarang} habis"
            stokTersedia < jumlahDiminta ->
                "Stok ${barang.namaBarang} tidak cukup. Tersedia: $stokTersedia, Diminta: $jumlahDiminta"
            else -> null
        }
    }


    fun formatStokMessage(barang: BarangResponse): String {
        val stok = barang.stok
        return if (stok == 0) {
            "Stok habis"
        } else {
            "Tersedia: $stok ${if (stok > 1) "items" else "item"}"
        }
    }

    fun getRemainingStok(barang: BarangResponse, jumlahOrder: Int): Int {
        val current = barang.stok
        return (current - jumlahOrder).coerceAtLeast(0)
    }
}

