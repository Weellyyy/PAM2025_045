package com.example.projectpammysql.ui.uicontroller.route

object DestinasiToko {
    const val route = "toko"
}

object DestinasiTokoEntry {
    const val route = "toko_entry"
}

object DestinasiTokoDetail {
    const val route = "toko_detail"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

object DestinasiTokoEdit {
    const val route = "toko_edit"
    const val tokoId = "tokoId"
    val routeWithArgs = "$route/{$tokoId}"
}
