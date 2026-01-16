package com.example.projectpammysql.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.models.OrderDetailItemRequest
import com.example.projectpammysql.data.models.OrderDetailItemResponse
import com.example.projectpammysql.data.repositories.RepositoryOrderDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class OrderDetailUiState {
    object Idle : OrderDetailUiState()
    object Loading : OrderDetailUiState()
    data class SuccessList(val orderDetailList: List<OrderDetailItemResponse>) : OrderDetailUiState()
    data class SuccessDetail(val orderDetail: OrderDetailItemResponse) : OrderDetailUiState()
    data class Success(val message: String) : OrderDetailUiState()
    data class Error(val message: String) : OrderDetailUiState()
}

class OrderDetailViewModel(
    private val repositoryOrderDetail: RepositoryOrderDetail
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrderDetailUiState>(OrderDetailUiState.Idle)
    val uiState: StateFlow<OrderDetailUiState> = _uiState

    fun getOrderDetailByOrderId(orderId: Int) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val response = repositoryOrderDetail.getOrderDetailByOrderId(orderId)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = OrderDetailUiState.SuccessList(response.body()!!)
                } else {
                    _uiState.value = OrderDetailUiState.Error("Gagal mengambil detail order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getOrderDetailById(id: Int) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val response = repositoryOrderDetail.getOrderDetailById(id)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = OrderDetailUiState.SuccessDetail(response.body()!!)
                } else {
                    _uiState.value = OrderDetailUiState.Error("Gagal mengambil detail: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun createOrderDetail(orderDetailRequest: OrderDetailItemRequest) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val response = repositoryOrderDetail.createOrderDetail(orderDetailRequest)
                if (response.isSuccessful) {
                    _uiState.value = OrderDetailUiState.Success("Detail order berhasil ditambahkan")
                } else {
                    _uiState.value = OrderDetailUiState.Error("Gagal menambah detail order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun updateOrderDetail(id: Int, orderDetailRequest: OrderDetailItemRequest) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val response = repositoryOrderDetail.updateOrderDetail(id, orderDetailRequest)
                if (response.isSuccessful) {
                    _uiState.value = OrderDetailUiState.Success("Detail order berhasil diperbarui")
                } else {
                    _uiState.value = OrderDetailUiState.Error("Gagal memperbarui detail order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun deleteOrderDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = OrderDetailUiState.Loading
            try {
                val response = repositoryOrderDetail.deleteOrderDetail(id)
                if (response.isSuccessful) {
                    _uiState.value = OrderDetailUiState.Success("Detail order berhasil dihapus")
                } else {
                    _uiState.value = OrderDetailUiState.Error("Gagal menghapus detail order: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = OrderDetailUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = OrderDetailUiState.Idle
    }
}

