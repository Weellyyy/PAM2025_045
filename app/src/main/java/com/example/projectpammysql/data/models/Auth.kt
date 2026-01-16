package com.example.projectpammysql.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int = 0,
    val username: String,
    val password: String,
    val role: String = "admin"
)
@Serializable

data class UserRequest(
    val username: String,
    val password: String
)
@Serializable

data class LoginResponse(
    val token: String
)
