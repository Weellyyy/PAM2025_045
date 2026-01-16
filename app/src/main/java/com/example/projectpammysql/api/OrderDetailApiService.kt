package com.example.projectpammysql.api

import com.example.projectpammysql.data.models.OrderDetailItemRequest
import com.example.projectpammysql.data.models.OrderDetailItemResponse
import retrofit2.Response
import retrofit2.http.*

interface OrderDetailApiService {
    @GET("api/order-detail/{orderId}")
    suspend fun getOrderDetailByOrderId(
        @Path("orderId") orderId: Int
    ): Response<List<OrderDetailItemResponse>>

    @GET("api/order-detail/{id}")
    suspend fun getOrderDetailById(
        @Path("id") id: Int
    ): Response<OrderDetailItemResponse>

    @POST("api/order-detail")
    suspend fun createOrderDetail(
        @Body orderDetailRequest: OrderDetailItemRequest
    ): Response<OrderDetailItemResponse>

    @PUT("api/order-detail/{id}")
    suspend fun updateOrderDetail(
        @Path("id") id: Int,
        @Body orderDetailRequest: OrderDetailItemRequest
    ): Response<OrderDetailItemResponse>

    @DELETE("api/order-detail/{id}")
    suspend fun deleteOrderDetail(
        @Path("id") id: Int
    ): Response<Unit>
}

