package com.example.projectpammysql.data.repositories

import com.example.projectpammysql.api.TokoApiService
import com.example.projectpammysql.data.models.TokoRequest
import com.example.projectpammysql.data.models.TokoResponse
import retrofit2.Response

interface RepositoryToko {
    suspend fun getAllToko(): Response<List<TokoResponse>>
    suspend fun getTokoById(id: Int): Response<TokoResponse>
    suspend fun createToko(tokoRequest: TokoRequest): Response<TokoResponse>
    suspend fun updateToko(id: Int, tokoRequest: TokoRequest): Response<TokoResponse>
    suspend fun deleteToko(id: Int): Response<Unit>
}

class NetworkRepositoryToko(
    private val tokoApiService: TokoApiService
) : RepositoryToko {

    override suspend fun getAllToko(): Response<List<TokoResponse>> {
        return tokoApiService.getAllToko()
    }

    override suspend fun getTokoById(id: Int): Response<TokoResponse> {
        return tokoApiService.getTokoById(id)
    }

    override suspend fun createToko(
        tokoRequest: TokoRequest
    ): Response<TokoResponse> {
        return tokoApiService.createToko(tokoRequest)
    }

    override suspend fun updateToko(
        id: Int,
        tokoRequest: TokoRequest
    ): Response<TokoResponse> {
        return tokoApiService.updateToko(id, tokoRequest)
    }

    override suspend fun deleteToko(id: Int): Response<Unit> {
        return tokoApiService.deleteToko(id)
    }
}
