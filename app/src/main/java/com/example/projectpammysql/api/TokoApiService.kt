package com.example.projectpammysql.api

import com.example.projectpammysql.data.models.TokoRequest
import com.example.projectpammysql.data.models.TokoResponse
import retrofit2.Response
import retrofit2.http.*

interface TokoApiService {
    @GET("api/toko")
    suspend fun getAllToko(): Response<List<TokoResponse>>

    @GET("api/toko/{id}")
    suspend fun getTokoById(
        @Path("id") id: Int
    ): Response<TokoResponse>

    @POST("api/toko")
    suspend fun createToko(
        @Body tokoRequest: TokoRequest
    ): Response<TokoResponse>

    @PUT("api/toko/{id}")
    suspend fun updateToko(
        @Path("id") id: Int,
        @Body tokoRequest: TokoRequest
    ): Response<TokoResponse>

    @DELETE("api/toko/{id}")
    suspend fun deleteToko(
        @Path("id") id: Int
    ): Response<Unit>
}
