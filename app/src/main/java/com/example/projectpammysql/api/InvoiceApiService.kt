package com.example.projectpammysql.api

import com.example.projectpammysql.data.models.InvoiceRequest
import com.example.projectpammysql.data.models.InvoiceResponse
import com.example.projectpammysql.data.models.InvoiceDetail
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface InvoiceApiService {
    @GET("api/invoice")
    suspend fun getAllInvoice(): Response<List<InvoiceResponse>>

    @GET("api/invoice/{id}")
    suspend fun getInvoiceById(
        @Path("id") id: Int
    ): Response<InvoiceResponse>

    @GET("api/invoice/order/{orderId}")
    suspend fun getInvoiceByOrderId(
        @Path("orderId") orderId: Int
    ): Response<InvoiceDetail>

    @POST("api/invoice")
    suspend fun createInvoice(
        @Body invoiceRequest: InvoiceRequest
    ): Response<InvoiceResponse>

    @DELETE("api/invoice/{id}")
    suspend fun deleteInvoice(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/invoice/{id}/cetak")
    @Streaming
    suspend fun generateInvoicePDF(
        @Path("id") id: Int
    ): Response<ResponseBody>
}
