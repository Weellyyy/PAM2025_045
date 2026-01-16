package com.example.projectpammysql.api

import com.example.projectpammysql.data.models.OrderDetail
import com.example.projectpammysql.data.models.OrderRequest
import com.example.projectpammysql.data.models.OrderResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderApiService {
    @GET("api/order")
    suspend fun getAllOrder(): Response<List<OrderResponse>>

    @GET("api/order/{id}")
    suspend fun getOrderById(
        @Path("id") id: Int
    ): Response<OrderResponse>

    @GET("api/order/{id}/details")
    suspend fun getOrderDetail(
        @Path("id") id: Int
    ): Response<OrderDetail>

    @POST("api/order")
    suspend fun createOrder(
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

    @PUT("api/order/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

    @DELETE("api/order/{id}")
    suspend fun deleteOrder(
        @Path("id") id: Int
    ): Response<Unit>
}
