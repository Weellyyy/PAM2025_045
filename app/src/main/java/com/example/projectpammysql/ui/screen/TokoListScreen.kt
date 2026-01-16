package com.example.projectpammysql.ui.screen

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.data.models.TokoResponse
import com.example.projectpammysql.ui.components.BottomNavigationDashboard
import com.example.projectpammysql.viewmodel.TokoUiState
import com.example.projectpammysql.viewmodel.TokoViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TokoListScreen(
    viewModel: TokoViewModel = viewModel(factory = PenyediaViewModel.Factory),
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
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllToko()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is TokoUiState.Success -> {
                viewModel.resetState()
                viewModel.getAllToko()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Kelola Toko") },
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
                Icon(Icons.Filled.Add, contentDescription = "Tambah Toko",
                        tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationDashboard(
                navigateToBarang = navigateToBarang,
                navigateToToko = navigateToToko,
                navigateToOrder = navigateToOrder,
                navigateToDashboard = navigateToDashboard,
                currentRoute = "toko"
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        when (uiState) {
            is TokoUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TokoUiState.SuccessList -> {
                val tokoList = (uiState as TokoUiState.SuccessList).tokoList
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tokoList) { toko ->
                        TokoCard(
                            toko = toko,
                            onEdit = { NavigateToEdit(toko.tokoId) },
                            onDelete = { viewModel.deleteToko(toko.tokoId) }
                        )
                    }
                }
            }

            is TokoUiState.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is TokoUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${(uiState as TokoUiState.Error).message}", color = Color.Red)
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
fun TokoCard(
    toko: TokoResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus toko '${toko.namaToko}'?") },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nama Toko",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = toko.namaToko,
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Alamat",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = toko.alamat ?: "-",
                        fontSize = 12.sp
                    )
                }

                Column {
                    Text(
                        text = "Kontak",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = toko.kontak ?: "-",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationDashboard(
    navigateToBarang: () -> Unit,
    navigateToToko: () -> Unit,
    navigateToOrder: () -> Unit,
    currentRoute: String = "barang"
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Dashboard",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("DASHBOARD", fontSize = 10.sp) },
            selected = currentRoute == "dashboard",
            onClick = {}
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Barang",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("BARANG", fontSize = 10.sp) },
            selected = currentRoute == "barang",
            onClick = navigateToBarang
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Toko",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("TOKO", fontSize = 10.sp) },
            selected = currentRoute == "toko",
            onClick = navigateToToko
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Order",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("TRANSAKSI", fontSize = 10.sp) },
            selected = currentRoute == "order",
            onClick = navigateToOrder
        )
    }
}
