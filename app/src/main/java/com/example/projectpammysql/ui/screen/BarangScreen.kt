package com.example.projectpammysql.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectpammysql.data.models.BarangResponse
import com.example.projectpammysql.ui.components.BottomNavigationDashboard
import com.example.projectpammysql.viewmodel.BarangViewModel
import com.example.projectpammysql.viewmodel.BarangUiState
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangListScreen(
    token: String,
    viewModel: BarangViewModel = viewModel(factory = PenyediaViewModel.Factory),
    NavigateBack: () -> Unit = {},
    NavigateToAdd: () -> Unit = {},
    NavigateToEdit: (Int) -> Unit = {},
    navigateToBarang: () -> Unit = {},
    navigateToToko: () -> Unit = {},
    navigateToOrder: () -> Unit = {},
    navigateToDashboard: () -> Unit = {},
    navigateLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value

    Log.d("BarangListScreen", "Current UI State: ${uiState::class.simpleName}")

    LaunchedEffect(Unit) {
        Log.d("BarangListScreen", "LaunchedEffect triggered")
        viewModel.getAllBarang()
    }

    LaunchedEffect(uiState) {
        Log.d("BarangListScreen", "UI State changed to: ${uiState::class.simpleName}")
        when (uiState) {
            is BarangUiState.SuccessList -> {
                Log.d("BarangListScreen", "SuccessList received with ${uiState.barangList.size} items")
            }
            is BarangUiState.Success -> {
                Log.d("BarangListScreen", "Success state: ${uiState.message}")
                // Auto-refresh list setelah create/update/delete
                viewModel.getAllBarang()
                viewModel.resetState()
            }
            is BarangUiState.Error -> {
                Log.e("BarangListScreen", "Error state: ${uiState.message}")
                viewModel.resetState()
            }
            else -> {
                Log.d("BarangListScreen", "Other state: ${uiState::class.simpleName}")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Kelola Barang") },
                navigationIcon = {
                    IconButton(onClick = NavigateBack) {
                        Text("‹", fontSize = 24.sp)
                    }
                }
            )
            IconButton(onClick = navigateLogout) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = NavigateToAdd,
                containerColor = Color.Black
            ) {
                Icon(Icons.Filled.Add,
                    contentDescription = "Tambah Barang",
                    tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationDashboard(
                navigateToBarang = navigateToBarang,
                navigateToToko = navigateToToko,
                navigateToOrder = navigateToOrder,
                navigateToDashboard = navigateToDashboard,
                currentRoute = "barang"
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        when (uiState) {
            is BarangUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Loading...")
                }
            }

            is BarangUiState.SuccessList -> {
                Log.d("BarangListScreen", "Rendering list with ${uiState.barangList.size} items")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.barangList) { barang ->
                        BarangCard(
                            barang = barang,
                            onEdit = { NavigateToEdit(barang.barangId) },
                            onDelete = { viewModel.deleteBarang(barang.barangId) }
                        )
                    }
                }
            }

            is BarangUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${uiState.message}", color = Color.Red)
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tidak ada data")
                }
            }
        }
    }
}

@Composable
fun BarangCard(
    barang: BarangResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus barang '${barang.namaBarang}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Gambar Barang
            if (!barang.gambarUrl.isNullOrEmpty()) {
                val imageUrl = if (barang.gambarUrl.startsWith("http")) {
                    barang.gambarUrl
                } else {
                    // Handle both /uploads dan /public path
                    val path = if (barang.gambarUrl.startsWith("/public")) {
                        barang.gambarUrl
                    } else if (barang.gambarUrl.startsWith("/uploads")) {
                        barang.gambarUrl.replace("/uploads", "/public")
                    } else {
                        "/public/barang/${barang.gambarUrl}"
                    }
                    "http://10.0.2.2:3000$path"
                }

                Log.d("BarangCard", "Raw gambarUrl: ${barang.gambarUrl}")
                Log.d("BarangCard", "Final imageUrl: $imageUrl")

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = barang.namaBarang,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        onError = { error ->
                            Log.e("BarangCard", "Error loading image")
                            Log.e("BarangCard", "URL: $imageUrl")
                            Log.e("BarangCard", "Error: ${error.result.throwable?.message}")
                        },
                        onSuccess = {
                            Log.d("BarangCard", "✅ Image loaded successfully: $imageUrl")
                        }
                    )

                    // Fallback icon jika gambar kosong
                    if (barang.gambarUrl.isEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Image,
                            contentDescription = "No Image",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nama Barang",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = barang.namaBarang,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color.Blue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Stok",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = barang.stok.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = "Harga",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Rp. ${barang.harga}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
