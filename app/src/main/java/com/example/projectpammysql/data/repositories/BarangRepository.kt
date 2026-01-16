package com.example.projectpammysql.data.repositories

import com.example.projectpammysql.api.BarangApiService
import com.example.projectpammysql.data.models.BarangRequest
import com.example.projectpammysql.data.models.BarangResponse
import retrofit2.Response

interface RepositoryBarang {
    suspend fun getAllBarang(): Response<List<BarangResponse>>
    suspend fun getBarangById(id: Int): Response<BarangResponse>
    suspend fun createBarang(barangRequest: BarangRequest): Response<BarangResponse>
    suspend fun updateBarang(id: Int, barangRequest: BarangRequest): Response<BarangResponse>
    suspend fun deleteBarang(id: Int): Response<Unit>
}

class NetworkRepositoryBarang(
    private val barangApiService: BarangApiService
) : RepositoryBarang {

    override suspend fun getAllBarang(): Response<List<BarangResponse>> {
        return barangApiService.getAllBarang()
    }

    override suspend fun getBarangById(id: Int): Response<BarangResponse> {
        return barangApiService.getBarangById(id)
    }

    override suspend fun createBarang(
        barangRequest: BarangRequest
    ): Response<BarangResponse> {
        return barangApiService.createBarang(barangRequest)
    }

    override suspend fun updateBarang(
        id: Int,
        barangRequest: BarangRequest
    ): Response<BarangResponse> {
        return barangApiService.updateBarang(id, barangRequest)
    }

    override suspend fun deleteBarang(id: Int): Response<Unit> {
        return barangApiService.deleteBarang(id)
    }
}
