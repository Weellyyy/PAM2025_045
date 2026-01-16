package com.example.projectpammysql.data.repositories

import android.app.Application
import android.content.Context
import com.example.projectpammysql.api.AuthApiService
import com.example.projectpammysql.api.AuthInterceptor
import com.example.projectpammysql.api.BarangApiService
import com.example.projectpammysql.api.InvoiceApiService
import com.example.projectpammysql.api.OrderApiService
import com.example.projectpammysql.api.OrderDetailApiService
import com.example.projectpammysql.api.TokoApiService
import com.example.projectpammysql.utils.TokenManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface ContainerApp {
    val repositoriAuth: RepositoryAuth
    val repositoriBarang: RepositoryBarang
    val repositoriToko: RepositoryToko
    val repositoriOrder: RepositoryOrder
    val repositoriOrderDetail: RepositoryOrderDetail
    val repositoriInvoice: RepositoryInvoice
    val tokenManager: TokenManager
}

class DefaultContainerApp(private val context: Context) : ContainerApp {
    private val baseUrl = "http://10.0.2.2:3000/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    override val tokenManager: TokenManager by lazy {
        TokenManager(context)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(AuthInterceptor(tokenManager))
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                namingStrategy = JsonNamingStrategy.SnakeCase
            }.asConverterFactory("application/json".toMediaType())
        )
        .client(client)
        .build()

    private val authService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    private val barangService: BarangApiService by lazy {
        retrofit.create(BarangApiService::class.java)
    }

    private val tokoService: TokoApiService by lazy {
        retrofit.create(TokoApiService::class.java)
    }

    private val orderService: OrderApiService by lazy {
        retrofit.create(OrderApiService::class.java)
    }

    private val invoiceService: InvoiceApiService by lazy {
        retrofit.create(InvoiceApiService::class.java)
    }

    private val orderDetailService: OrderDetailApiService by lazy {
        retrofit.create(OrderDetailApiService::class.java)
    }


    override val repositoriAuth: RepositoryAuth by lazy {
        NetworkRepositoryAuth(authService)
    }

    override val repositoriBarang: RepositoryBarang by lazy {
        NetworkRepositoryBarang(barangService)
    }

    override val repositoriToko: RepositoryToko by lazy {
        NetworkRepositoryToko(tokoService)
    }

    override val repositoriOrder: RepositoryOrder by lazy {
        NetworkRepositoryOrder(orderService)
    }

    override val repositoriInvoice: RepositoryInvoice by lazy {
        NetworkRepositoryInvoice(invoiceService, context)
    }

    override val repositoriOrderDetail: RepositoryOrderDetail by lazy {
        NetworkRepositoryOrderDetail(orderDetailService)
    }

}

class AplikasiDataApp : Application() {
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        container = DefaultContainerApp(this)
    }
}
