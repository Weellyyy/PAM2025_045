package com.example.projectpammysql.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.models.OrderRequest
import com.example.projectpammysql.data.models.OrderResponse
import com.example.projectpammysql.data.models.OrderDetail
import com.example.projectpammysql.data.repositories.RepositoryOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class OrderUiState {
    object Idle : OrderUiState()
    object Loading : OrderUiState()
    data class SuccessList(val orderList: List<OrderResponse>) : OrderUiState()
    data class SuccessDetail(val order: OrderDetail) : OrderUiState()
    data class Success(val message: String) : OrderUiState()
    data class Error(val message: String) : OrderUiState()
}

class OrderViewModel(
    private val repositoryOrder: RepositoryOrder
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val uiState: StateFlow<OrderUiState> = _uiState

    fun getAllOrder() {
        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading
            try {
                val response = repositoryOrder.getAllOrder()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = OrderUiState.SuccessList(response.body()!!)
                } else {
                    _uiState.value = OrderUiState.Error("Gagal mengambil data order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getOrderDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading
            try {
                val response = repositoryOrder.getOrderDetail(id)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = OrderUiState.SuccessDetail(response.body()!!)
                } else {
                    _uiState.value = OrderUiState.Error("Gagal mengambil detail order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderUiState.Error("Error: ${e.message}")
            }
        }
    }

    private var isSubmitting = false

    fun createOrder(orderRequest: OrderRequest) {
        if (isSubmitting) {
            return // 🚫 cegah submit ganda
        }

        isSubmitting = true

        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading
            try {
                // Validasi di Kotlin side sebelum kirim ke backend
                val validationError = validateOrderItems(orderRequest)
                if (validationError != null) {
                    _uiState.value = OrderUiState.Error(validationError)
                    return@launch
                }

                val response = repositoryOrder.createOrder(orderRequest)
                if (response.isSuccessful) {
                    _uiState.value =
                        OrderUiState.Success("Order berhasil dibuat dan stok otomatis berkurang")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    val errorMsg = if (errorBody.contains("stok", ignoreCase = true)) {
                        "Stok tidak cukup. Silahkan periksa ketersediaan barang"
                    } else {
                        "Gagal membuat order: ${response.message()}"
                    }
                    _uiState.value = OrderUiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = OrderUiState.Error("Error: ${e.message}")
            } finally {
                isSubmitting = false // ✅ reset guard
            }
        }
    }


    /**
     * Validasi order items sebelum dikirim ke backend
     * Cek: toko ada, items tidak kosong, jumlah valid
     */
    private fun validateOrderItems(orderRequest: OrderRequest): String? {
        if (orderRequest.items.isEmpty()) {
            return "Minimal harus ada 1 item dalam order"
        }

        for (item in orderRequest.items) {
            if (item.barangId <= 0) {
                return "Barang tidak valid"
            }
            if (item.jumlah <= 0) {
                return "Jumlah item harus lebih dari 0"
            }
            if (item.hargaSatuan <= 0.0) {
                return "Harga satuan tidak valid"
            }
        }

        return null
    }

    fun updateOrder(id: Int, orderRequest: OrderRequest) {
        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading
            try {
                val response = repositoryOrder.updateOrder(id, orderRequest)
                if (response.isSuccessful) {
                    _uiState.value = OrderUiState.Success("Order berhasil diperbarui")
                } else {
                    _uiState.value = OrderUiState.Error("Gagal memperbarui order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun deleteOrder(id: Int) {
        viewModelScope.launch {
            _uiState.value = OrderUiState.Loading
            try {
                val response = repositoryOrder.deleteOrder(id)
                if (response.isSuccessful) {
                    _uiState.value = OrderUiState.Success("Order berhasil dihapus")
                } else {
                    _uiState.value = OrderUiState.Error("Gagal menghapus order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = OrderUiState.Idle
    }
}
