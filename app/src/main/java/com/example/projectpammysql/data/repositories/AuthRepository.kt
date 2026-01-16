package com.example.projectpammysql.data.repositories

import com.example.projectpammysql.api.AuthApiService
import com.example.projectpammysql.data.models.LoginResponse
import com.example.projectpammysql.data.models.UserRequest
import retrofit2.Response

interface RepositoryAuth {
    suspend fun login(userRequest: UserRequest): Response<LoginResponse>
    suspend fun register(userRequest: UserRequest): Response<LoginResponse>
}

class NetworkRepositoryAuth(
    private val authApiService: AuthApiService
) : RepositoryAuth {

    override suspend fun login(userRequest: UserRequest): Response<LoginResponse> {
        return authApiService.login(userRequest)
    }

    override suspend fun register(userRequest: UserRequest): Response<LoginResponse> {
        return authApiService.register(userRequest)
    }
}
