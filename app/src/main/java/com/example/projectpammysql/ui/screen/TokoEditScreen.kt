package com.example.projectpammysql.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.data.models.TokoRequest
import com.example.projectpammysql.viewmodel.TokoUiState
import com.example.projectpammysql.viewmodel.TokoViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokoEditScreen(
    tokoId: Int,
    viewModel: TokoViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var namaToko by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var kontak by remember { mutableStateOf("") }
    var isLoaded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tokoId) {
        viewModel.getTokoById(tokoId)
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is TokoUiState.SuccessDetail -> {
                if (!isLoaded) {
                    val toko = (uiState as TokoUiState.SuccessDetail).toko
                    namaToko = toko.namaToko
                    alamat = toko.alamat ?: ""
                    kontak = toko.kontak ?: ""
                    isLoaded = true
                }
            }
            is TokoUiState.Success -> {
                viewModel.resetState()
                navigateBack()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Toko") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Text("‹", fontSize = 24.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState) {
                is TokoUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is TokoUiState.Error -> {
                    Text(
                        text = (uiState as TokoUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    OutlinedTextField(
                        value = namaToko,
                        onValueChange = { namaToko = it },
                        label = { Text("Nama Toko") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text("Alamat") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = kontak,
                        onValueChange = { kontak = it },
                        label = { Text("Kontak") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            val tokoRequest = TokoRequest(
                                namaToko = namaToko,
                                alamat = alamat.ifEmpty { null },
                                kontak = kontak.ifEmpty { null }
                            )
                            viewModel.updateToko(tokoId, tokoRequest)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        enabled = namaToko.isNotEmpty()
                    ) {
                        Text("Simpan Perubahan")
                    }
                }
            }
        }
    }
}

@Composable
fun Spacer(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Spacer(modifier = modifier.fillMaxWidth())
}

