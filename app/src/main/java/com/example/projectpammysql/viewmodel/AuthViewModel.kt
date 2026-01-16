package com.example.projectpammysql.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpammysql.data.models.LoginResponse
import com.example.projectpammysql.data.models.UserRequest
import com.example.projectpammysql.data.repositories.RepositoryAuth
import com.example.projectpammysql.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val userResponse: LoginResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel(
    private val repositoryAuth: RepositoryAuth,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Login attempt for username: $username")
            _uiState.value = AuthUiState.Loading
            try {
                val userRequest = UserRequest(username, password)
                val response = repositoryAuth.login(userRequest)

                Log.d("AuthViewModel", "Login response code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    Log.d("AuthViewModel", "Login successful, token received: ${loginResponse.token.take(20)}...")

                    // Simpan token setelah login berhasil
                    tokenManager.saveToken(loginResponse.token)
                    Log.d("AuthViewModel", "Token saved to TokenManager")

                    _uiState.value = AuthUiState.Success(loginResponse)
                } else {
                    Log.e("AuthViewModel", "Login failed: ${response.message()}")
                    _uiState.value = AuthUiState.Error("Login gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login exception: ${e.message}", e)
                _uiState.value = AuthUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Register attempt for username: $username")
            _uiState.value = AuthUiState.Loading
            try {
                val userRequest = UserRequest(username, password)
                val response = repositoryAuth.register(userRequest)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("AuthViewModel", "Register successful")
                    _uiState.value = AuthUiState.Success(response.body()!!)
                } else {
                    Log.e("AuthViewModel", "Register failed: ${response.message()}")
                    _uiState.value = AuthUiState.Error("Register gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Register exception: ${e.message}", e)
                _uiState.value = AuthUiState.Error("Error: ${e.message}")
            }
        }
    }
}
