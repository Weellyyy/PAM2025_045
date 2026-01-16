package com.example.projectpammysql.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_prefs")

class TokenManager(private val context: Context) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        Log.d("TokenManager", "Saving token: ${token.take(20)}...")
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
        Log.d("TokenManager", "Token saved successfully")
    }

    fun getTokenFlow(): Flow<String?> = context.dataStore.data.map { preferences ->
        val token = preferences[TOKEN_KEY]
        Log.d("TokenManager", "Getting token from DataStore: ${if (token != null) "Found (${token.take(20)}...)" else "Not found"}")
        token
    }

    suspend fun clearToken() {
        Log.d("TokenManager", "Clearing token")
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
        Log.d("TokenManager", "Token cleared")
    }
}
