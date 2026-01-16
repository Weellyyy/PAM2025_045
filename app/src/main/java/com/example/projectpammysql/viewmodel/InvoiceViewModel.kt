package com.example.projectpammysql.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.models.InvoiceRequest
import com.example.projectpammysql.data.models.InvoiceResponse
import com.example.projectpammysql.data.models.InvoiceDetail
import com.example.projectpammysql.data.repositories.RepositoryInvoice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class InvoiceUiState {
    object Idle : InvoiceUiState()
    object Loading : InvoiceUiState()
    data class SuccessList(val invoiceList: List<InvoiceResponse>) : InvoiceUiState()
    data class SuccessDetail(val invoice: InvoiceDetail) : InvoiceUiState()
    data class Success(val message: String) : InvoiceUiState()
    data class Error(val message: String) : InvoiceUiState()
}

class InvoiceViewModel(
    private val repositoryInvoice: RepositoryInvoice
) : ViewModel() {

    private val _uiState = MutableStateFlow<InvoiceUiState>(InvoiceUiState.Idle)
    val uiState: StateFlow<InvoiceUiState> = _uiState

    fun getAllInvoice() {
        viewModelScope.launch {
            _uiState.value = InvoiceUiState.Loading
            try {
                val response = repositoryInvoice.getAllInvoice()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = InvoiceUiState.SuccessList(response.body()!!)
                } else {
                    _uiState.value = InvoiceUiState.Error("Gagal mengambil data invoice: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = InvoiceUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getInvoiceByOrderId(orderId: Int) {
        viewModelScope.launch {
            _uiState.value = InvoiceUiState.Loading
            try {
                val response = repositoryInvoice.getInvoiceByOrderId(orderId)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = InvoiceUiState.SuccessDetail(response.body()!!)
                } else {
                    _uiState.value = InvoiceUiState.Error("Gagal mengambil invoice: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = InvoiceUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun createInvoice(invoiceRequest: InvoiceRequest) {
        viewModelScope.launch {
            _uiState.value = InvoiceUiState.Loading
            try {
                val response = repositoryInvoice.createInvoice(invoiceRequest)
                if (response.isSuccessful) {
                    _uiState.value = InvoiceUiState.Success("Invoice berhasil dibuat")
                } else {
                    _uiState.value = InvoiceUiState.Error("Gagal membuat invoice: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = InvoiceUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun deleteInvoice(id: Int) {
        viewModelScope.launch {
            _uiState.value = InvoiceUiState.Loading
            try {
                val response = repositoryInvoice.deleteInvoice(id)
                if (response.isSuccessful) {
                    _uiState.value = InvoiceUiState.Success("Invoice berhasil dihapus")
                } else {
                    _uiState.value = InvoiceUiState.Error("Gagal menghapus invoice: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = InvoiceUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun generateInvoicePDF(id: Int, context: Context) {
        viewModelScope.launch {
            _uiState.value = InvoiceUiState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    repositoryInvoice.generateInvoicePDF(id, context)
                }
                result.onSuccess { filePath ->
                    _uiState.value = InvoiceUiState.Success("PDF berhasil didownload ke: $filePath")
                }
                result.onFailure { error ->
                    _uiState.value = InvoiceUiState.Error("Gagal download PDF: ${error.message}")
                }
            } catch (e: Exception) {
                _uiState.value = InvoiceUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = InvoiceUiState.Idle
    }
}
