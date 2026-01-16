package com.example.projectpammysql.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.repositories.RepositoryBarang
import com.example.projectpammysql.data.repositories.RepositoryToko
import com.example.projectpammysql.data.repositories.RepositoryOrder
import com.example.projectpammysql.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val barangRepository: RepositoryBarang,
    private val tokoRepository: RepositoryToko,
    private val orderRepository: RepositoryOrder,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _jumlahBarang = MutableStateFlow(0)
    val jumlahBarang = _jumlahBarang.asStateFlow()

    private val _jumlahToko = MutableStateFlow(0)
    val jumlahToko = _jumlahToko.asStateFlow()

    private val _jumlahOrder = MutableStateFlow(0)
    val jumlahOrder = _jumlahOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadDashboard() {
        viewModelScope.launch {
            Log.d("DashboardViewModel", "loadDashboard called")
            _isLoading.value = true

            try {
                // Load Barang
                val barangResponse = barangRepository.getAllBarang()
                Log.d("DashboardViewModel", "Barang Response Code: ${barangResponse.code()}")
                Log.d("DashboardViewModel", "Barang Response Body: ${barangResponse.body()}")

                if (barangResponse.isSuccessful && barangResponse.body() != null) {
                    val count = barangResponse.body()?.size ?: 0
                    Log.d("DashboardViewModel", "Jumlah Barang: $count")
                    _jumlahBarang.value = count
                } else {
                    Log.e("DashboardViewModel", "Barang failed: ${barangResponse.message()}")
                }

                // Load Toko
                val tokoResponse = tokoRepository.getAllToko()
                Log.d("DashboardViewModel", "Toko Response Code: ${tokoResponse.code()}")

                if (tokoResponse.isSuccessful && tokoResponse.body() != null) {
                    val count = tokoResponse.body()?.size ?: 0
                    Log.d("DashboardViewModel", "Jumlah Toko: $count")
                    _jumlahToko.value = count
                } else {
                    Log.e("DashboardViewModel", "Toko failed: ${tokoResponse.message()}")
                }

                // Load Order
                val orderResponse = orderRepository.getAllOrder()
                Log.d("DashboardViewModel", "Order Response Code: ${orderResponse.code()}")

                if (orderResponse.isSuccessful && orderResponse.body() != null) {
                    val count = orderResponse.body()?.size ?: 0
                    Log.d("DashboardViewModel", "Jumlah Order: $count")
                    _jumlahOrder.value = count
                } else {
                    Log.e("DashboardViewModel", "Order failed: ${orderResponse.message()}")
                }

            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Exception in loadDashboard: ${e.message}", e)
            } finally {
                _isLoading.value = false
                Log.d("DashboardViewModel", "Loading finished")
            }
        }
    }
}
