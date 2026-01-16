package com.example.projectpammysql.ui.uicontroller.route

object DestinasiBarang {
    const val route = "barang"
}

object DestinasiBarangEntry {
    const val route = "barang_entry"
}

object DestinasiBarangDetail {
    const val route = "barang_detail"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object DestinasiBarangEdit {
    const val route = "barang_edit"
    const val barangId = "barangId"
    val routeWithArgs = "$route/{$barangId}"
}
