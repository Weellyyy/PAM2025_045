package com.example.projectpammysql.ui.uicontroller.route


object DestinasiInvoice {
    const val route = "invoice"
}

object DestinasiInvoiceEntry {
    const val route = "invoice_entry"
}

object DestinasiInvoiceDetail {
    const val route = "invoice_detail"
    const val orderId = "orderId"
    val routeWithArgs = "$route/{$orderId}"
}

object DestinasiInvoiceEdit {
    const val route = "invoice_edit"
    const val invoiceId = "invoiceId"
    val routeWithArgs = "$route/{$invoiceId}"
}
