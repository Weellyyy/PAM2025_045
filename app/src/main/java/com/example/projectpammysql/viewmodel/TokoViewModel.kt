package com.example.projectpammysql.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.models.TokoRequest
import com.example.projectpammysql.data.models.TokoResponse
import com.example.projectpammysql.data.repositories.RepositoryToko
import com.example.projectpammysql.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class TokoUiState {
    object Idle : TokoUiState()
    object Loading : TokoUiState()
    data class SuccessList(val tokoList: List<TokoResponse>) : TokoUiState()
    data class SuccessDetail(val toko: TokoResponse) : TokoUiState()
    data class Success(val message: String) : TokoUiState()
    data class Error(val message: String) : TokoUiState()
}

class TokoViewModel(
    private val repositoryToko: RepositoryToko,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<TokoUiState>(TokoUiState.Idle)
    val uiState: StateFlow<TokoUiState> = _uiState

    fun getAllToko() {
        viewModelScope.launch {
            _uiState.value = TokoUiState.Loading
            try {
                val response = repositoryToko.getAllToko()
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = TokoUiState.SuccessList(response.body()!!)
                } else {
                    _uiState.value = TokoUiState.Error("Gagal mengambil data toko: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = TokoUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getTokoById(id: Int) {
        viewModelScope.launch {
            _uiState.value = TokoUiState.Loading
            try {
                val response = repositoryToko.getTokoById(id)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = TokoUiState.SuccessDetail(response.body()!!)
                } else {
                    _uiState.value = TokoUiState.Error("Gagal mengambil detail toko: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = TokoUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun createToko(tokoRequest: TokoRequest) {
        viewModelScope.launch {
            _uiState.value = TokoUiState.Loading
            try {
                val response = repositoryToko.createToko(tokoRequest)
                if (response.isSuccessful) {
                    _uiState.value = TokoUiState.Success("Toko berhasil ditambahkan")
                } else {
                    _uiState.value = TokoUiState.Error("Gagal menambah toko: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = TokoUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun updateToko(id: Int, tokoRequest: TokoRequest) {
        viewModelScope.launch {
            _uiState.value = TokoUiState.Loading
            try {
                val response = repositoryToko.updateToko(id, tokoRequest)
                if (response.isSuccessful) {
                    _uiState.value = TokoUiState.Success("Toko berhasil diperbarui")
                } else {
                    _uiState.value = TokoUiState.Error("Gagal memperbarui toko: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = TokoUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun deleteToko(id: Int) {
        viewModelScope.launch {
            _uiState.value = TokoUiState.Loading
            try {
                val response = repositoryToko.deleteToko(id)
                if (response.isSuccessful) {
                    _uiState.value = TokoUiState.Success("Toko berhasil dihapus")
                } else {
                    _uiState.value = TokoUiState.Error("Gagal menghapus toko: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = TokoUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = TokoUiState.Idle
    }
}
