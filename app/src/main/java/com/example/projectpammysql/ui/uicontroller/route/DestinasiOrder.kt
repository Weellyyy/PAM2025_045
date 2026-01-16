package com.example.projectpammysql.ui.uicontroller.route

object DestinasiOrder {
    const val route = "order"
}

object DestinasiOrderEntry {
    const val route = "order_entry"
}

object DestinasiOrderDetail {
    const val route = "order_detail"
    const val orderId = "orderId"
    val routeWithArgs = "$route/{$orderId}"
}

object DestinasiOrderEdit {
    const val route = "order_edit"
    const val orderId = "orderId"
    val routeWithArgs = "$route/{$orderId}"
}