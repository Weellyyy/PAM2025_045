package com.example.projectpammysql.api

import com.example.projectpammysql.data.models.BarangRequest
import com.example.projectpammysql.data.models.BarangResponse
import retrofit2.Response
import retrofit2.http.*

interface BarangApiService {
    @GET("api/barang")
    suspend fun getAllBarang(): Response<List<BarangResponse>>

    @GET("api/barang/{id}")
    suspend fun getBarangById(
        @Path("id") id: Int
    ): Response<BarangResponse>

    @POST("api/barang")
    suspend fun createBarang(
        @Body barangRequest: BarangRequest
    ): Response<BarangResponse>

    @PUT("api/barang/{id}")
    suspend fun updateBarang(
        @Path("id") id: Int,
        @Body barangRequest: BarangRequest
    ): Response<BarangResponse>

    @DELETE("api/barang/{id}")
    suspend fun deleteBarang(
        @Path("id") id: Int
    ): Response<Unit>
}
