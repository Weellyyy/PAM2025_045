package com.example.projectpammysql.data.repositories

import com.example.projectpammysql.api.OrderDetailApiService
import com.example.projectpammysql.data.models.OrderDetailItemRequest
import com.example.projectpammysql.data.models.OrderDetailItemResponse
import retrofit2.Response

interface RepositoryOrderDetail {
    suspend fun getOrderDetailByOrderId(orderId: Int): Response<List<OrderDetailItemResponse>>
    suspend fun getOrderDetailById(id: Int): Response<OrderDetailItemResponse>
    suspend fun createOrderDetail(orderDetailRequest: OrderDetailItemRequest): Response<OrderDetailItemResponse>
    suspend fun updateOrderDetail(id: Int, orderDetailRequest: OrderDetailItemRequest): Response<OrderDetailItemResponse>
    suspend fun deleteOrderDetail(id: Int): Response<Unit>
}

class NetworkRepositoryOrderDetail(
    private val orderDetailApiService: OrderDetailApiService
) : RepositoryOrderDetail {

    override suspend fun getOrderDetailByOrderId(orderId: Int): Response<List<OrderDetailItemResponse>> {
        return orderDetailApiService.getOrderDetailByOrderId(orderId)
    }

    override suspend fun getOrderDetailById(id: Int): Response<OrderDetailItemResponse> {
        return orderDetailApiService.getOrderDetailById(id)
    }

    override suspend fun createOrderDetail(
        orderDetailRequest: OrderDetailItemRequest
    ): Response<OrderDetailItemResponse> {
        return orderDetailApiService.createOrderDetail(orderDetailRequest)
    }

    override suspend fun updateOrderDetail(
        id: Int,
        orderDetailRequest: OrderDetailItemRequest
    ): Response<OrderDetailItemResponse> {
        return orderDetailApiService.updateOrderDetail(id, orderDetailRequest)
    }

    override suspend fun deleteOrderDetail(id: Int): Response<Unit> {
        return orderDetailApiService.deleteOrderDetail(id)
    }
}

