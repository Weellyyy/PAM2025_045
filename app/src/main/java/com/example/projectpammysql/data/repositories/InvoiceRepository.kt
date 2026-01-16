package com.example.projectpammysql.data.repositories

import android.content.Context
import com.example.projectpammysql.api.InvoiceApiService
import com.example.projectpammysql.data.models.InvoiceRequest
import com.example.projectpammysql.data.models.InvoiceResponse
import com.example.projectpammysql.data.models.InvoiceDetail
import retrofit2.Response
import java.io.File

interface RepositoryInvoice {
    suspend fun getAllInvoice(): Response<List<InvoiceResponse>>
    suspend fun getInvoiceById(id: Int): Response<InvoiceResponse>
    suspend fun getInvoiceByOrderId(orderId: Int): Response<InvoiceDetail>
    suspend fun createInvoice(invoiceRequest: InvoiceRequest): Response<InvoiceResponse>
    suspend fun deleteInvoice(id: Int): Response<Unit>
    suspend fun generateInvoicePDF(id: Int, context: Context): Result<String>
}

class NetworkRepositoryInvoice(
    private val invoiceApiService: InvoiceApiService,
    private val context: Context
) : RepositoryInvoice {

    override suspend fun getAllInvoice(): Response<List<InvoiceResponse>> {
        return invoiceApiService.getAllInvoice()
    }

    override suspend fun getInvoiceById(id: Int): Response<InvoiceResponse> {
        return invoiceApiService.getInvoiceById(id)
    }

    override suspend fun getInvoiceByOrderId(orderId: Int): Response<InvoiceDetail> {
        return invoiceApiService.getInvoiceByOrderId(orderId)
    }

    override suspend fun createInvoice(
        invoiceRequest: InvoiceRequest
    ): Response<InvoiceResponse> {
        return invoiceApiService.createInvoice(invoiceRequest)
    }

    override suspend fun deleteInvoice(id: Int): Response<Unit> {
        return invoiceApiService.deleteInvoice(id)
    }

    override suspend fun generateInvoicePDF(id: Int, context: Context): Result<String> {
        return try {
            val response = invoiceApiService.generateInvoicePDF(id)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val fileName = "invoice_$id.pdf"
                val downloadsDir = File(context.getExternalFilesDir(null), "Downloads")
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                val file = File(downloadsDir, fileName)
                file.outputStream().use { output ->
                    body.byteStream().use { input ->
                        input.copyTo(output)
                    }
                }
                Result.success(file.absolutePath)
            } else {
                Result.failure(Exception("Gagal download PDF: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
