package com.example.projectpammysql.api

import com.example.projectpammysql.data.models.LoginResponse
import com.example.projectpammysql.data.models.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: UserRequest
    ): Response<LoginResponse>


    @POST("api/auth/register")
    suspend fun register(
        @Body userRequest: UserRequest
    ): Response<LoginResponse>
}
