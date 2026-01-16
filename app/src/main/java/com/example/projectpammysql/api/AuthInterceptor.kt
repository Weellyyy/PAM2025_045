package com.example.projectpammysql.api

import android.util.Log
import com.example.projectpammysql.utils.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getTokenFlow().first()
        }

        Log.d("AuthInterceptor", "Token from TokenManager: ${if (token.isNullOrEmpty()) "Empty/Null" else "Available (${token.take(20)}...)"}")

        val request = chain.request()
        val newRequest = if (!token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "Adding Bearer token to request: ${request.url}")
            request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            Log.w("AuthInterceptor", "No token available, proceeding without Authorization header")
            request
        }

        return chain.proceed(newRequest)
    }
}
