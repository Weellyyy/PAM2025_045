package com.example.projectpammysql.data.repositories

import com.example.projectpammysql.api.OrderApiService
import com.example.projectpammysql.data.models.OrderRequest
import com.example.projectpammysql.data.models.OrderResponse
import com.example.projectpammysql.data.models.OrderDetail
import retrofit2.Response

interface RepositoryOrder {
    suspend fun getAllOrder(): Response<List<OrderResponse>>
    suspend fun getOrderById(id: Int): Response<OrderResponse>
    suspend fun getOrderDetail(id: Int): Response<OrderDetail>
    suspend fun createOrder(orderRequest: OrderRequest): Response<OrderResponse>
    suspend fun updateOrder(id: Int, orderRequest: OrderRequest): Response<OrderResponse>
    suspend fun deleteOrder(id: Int): Response<Unit>
}

class NetworkRepositoryOrder(
    private val orderApiService: OrderApiService
) : RepositoryOrder {

    override suspend fun getAllOrder(): Response<List<OrderResponse>> {
        return orderApiService.getAllOrder()
    }

    override suspend fun getOrderById(id: Int): Response<OrderResponse> {
        return orderApiService.getOrderById(id)
    }

    override suspend fun getOrderDetail(id: Int): Response<OrderDetail> {
        return orderApiService.getOrderDetail(id)
    }

    override suspend fun createOrder(
        orderRequest: OrderRequest
    ): Response<OrderResponse> {
        return orderApiService.createOrder(orderRequest)
    }

    override suspend fun updateOrder(
        id: Int,
        orderRequest: OrderRequest
    ): Response<OrderResponse> {
        return orderApiService.updateOrder(id, orderRequest)
    }

    override suspend fun deleteOrder(id: Int): Response<Unit> {
        return orderApiService.deleteOrder(id)
    }
}
