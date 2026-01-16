package com.example.projectpammysql.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.models.BarangRequest
import com.example.projectpammysql.data.models.BarangResponse
import com.example.projectpammysql.data.repositories.RepositoryBarang
import com.example.projectpammysql.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class BarangUiState {
    object Idle : BarangUiState()
    object Loading : BarangUiState()
    data class SuccessList(val barangList: List<BarangResponse>) : BarangUiState()
    data class SuccessDetail(val barang: BarangResponse) : BarangUiState()
    data class Success(val message: String) : BarangUiState()
    data class Error(val message: String) : BarangUiState()
}

class BarangViewModel(
    private val repositoryBarang: RepositoryBarang,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<BarangUiState>(BarangUiState.Idle)
    val uiState: StateFlow<BarangUiState> = _uiState

    fun getAllBarang() {
        viewModelScope.launch {

            _uiState.value = BarangUiState.Loading

            try {
                val response = repositoryBarang.getAllBarang()
                Log.d("BarangViewModel", "Response received: ${response.code()}")
                Log.d("BarangViewModel", "Response body: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    val barangList = response.body()!!
                    Log.d("BarangViewModel", "Barang list size: ${barangList.size}")
                    barangList.forEachIndexed { index, barang ->
                        Log.d("BarangViewModel", "Barang $index: ${barang.namaBarang}, stok: ${barang.stok}, harga: ${barang.harga}")
                    }
                    _uiState.value = BarangUiState.SuccessList(barangList)
                    Log.d("BarangViewModel", "State set to SuccessList")
                } else {
                    Log.e("BarangViewModel", "Response not successful: ${response.message()}, code: ${response.code()}")
                    _uiState.value = BarangUiState.Error("Gagal mengambil data: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Exception in getAllBarang: ${e.message}", e)
                _uiState.value = BarangUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun getBarangById(id: Int) {
        viewModelScope.launch {
            _uiState.value = BarangUiState.Loading
            try {
                val response = repositoryBarang.getBarangById(id)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = BarangUiState.SuccessDetail(response.body()!!)
                } else {
                    _uiState.value = BarangUiState.Error("Gagal mengambil detail: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = BarangUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun createBarang(barangRequest: BarangRequest) {
        viewModelScope.launch {
            _uiState.value = BarangUiState.Loading
            try {
                val response = repositoryBarang.createBarang(barangRequest)
                if (response.isSuccessful) {
                    _uiState.value = BarangUiState.Success("Barang berhasil ditambahkan")
                } else {
                    _uiState.value = BarangUiState.Error("Gagal menambah barang: ${response.message()}")
                }
            } catch (e: Exception) {
                _uiState.value = BarangUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun updateBarang(id: Int, barangRequest: BarangRequest) {
        viewModelScope.launch {
            Log.d("BarangViewModel", "updateBarang called for id: $id")
            _uiState.value = BarangUiState.Loading
            try {
                Log.d("BarangViewModel", "Updating barang: ${barangRequest.namaBarang}")
                val response = repositoryBarang.updateBarang(id, barangRequest)
                Log.d("BarangViewModel", "Update response code: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("BarangViewModel", "Barang updated successfully")
                    _uiState.value = BarangUiState.Success("Barang berhasil diperbarui")
                } else {
                    Log.e("BarangViewModel", "Update failed: ${response.message()}")
                    _uiState.value = BarangUiState.Error("Gagal memperbarui barang: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Exception in updateBarang: ${e.message}", e)
                _uiState.value = BarangUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun deleteBarang(id: Int) {
        viewModelScope.launch {
            Log.d("BarangViewModel", "deleteBarang called for id: $id")
            _uiState.value = BarangUiState.Loading
            try {
                val response = repositoryBarang.deleteBarang(id)
                Log.d("BarangViewModel", "Delete response code: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("BarangViewModel", "Barang deleted successfully")
                    _uiState.value = BarangUiState.Success("Barang berhasil dihapus")
                } else {
                    Log.e("BarangViewModel", "Delete failed: ${response.message()}")
                    _uiState.value = BarangUiState.Error("Gagal menghapus barang: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("BarangViewModel", "Exception in deleteBarang: ${e.message}", e)
                _uiState.value = BarangUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = BarangUiState.Idle
    }
}
