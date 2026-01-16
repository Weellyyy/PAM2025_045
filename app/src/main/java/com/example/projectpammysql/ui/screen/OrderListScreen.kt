package com.example.projectpammysql.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.data.models.OrderResponse
import com.example.projectpammysql.ui.components.BottomNavigationDashboard
import com.example.projectpammysql.viewmodel.OrderUiState
import com.example.projectpammysql.viewmodel.OrderViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
    viewModel: OrderViewModel = viewModel(factory = PenyediaViewModel.Factory),
    NavigateBack: () -> Unit = {},
    NavigateToAdd: () -> Unit = {},
    NavigateToDetail: (Int) -> Unit = {},
    navigateToBarang: () -> Unit = {},
    navigateToToko: () -> Unit = {},
    navigateToOrder: () -> Unit = {},
    navigateToDashboard: () -> Unit = {},
    navigateLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllOrder()
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is OrderUiState.Success -> {
                viewModel.resetState()
                viewModel.getAllOrder()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daftar Order") },
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
                Icon(Icons.Filled.Add, contentDescription = "Tambah Order",
                    tint = Color.White)
            }
        },
        bottomBar = {
            BottomNavigationDashboard(
                navigateToBarang = navigateToBarang,
                navigateToToko = navigateToToko,
                navigateToOrder = navigateToOrder,
                navigateToDashboard = navigateToDashboard,
                currentRoute = "order"
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        when (uiState) {
            is OrderUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is OrderUiState.SuccessList -> {
                val orderList = (uiState as OrderUiState.SuccessList).orderList
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orderList) { order ->
                        OrderCard(
                            order = order,
                            onDetail = { NavigateToDetail(order.orderId) },
                            onDelete = { viewModel.deleteOrder(order.orderId) }
                        )
                    }
                }
            }

            is OrderUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${(uiState as OrderUiState.Error).message}", color = Color.Red)
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
fun OrderCard(
    order: OrderResponse,
    onDetail: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus") },
            text = { Text("Yakin hapus order ini?") },
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
        modifier = modifier.fillMaxWidth(),
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
                    Text("Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(order.namaToko, fontSize = 12.sp, color = Color.Gray)
                }
                Text(order.status, fontSize = 10.sp, color = Color(0xFF4CAF50))
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Username", fontSize = 10.sp, color = Color.Gray)
                    Text(order.username ?: "-", fontSize = 12.sp)
                }
                Column {
                    Text("Tanggal", fontSize = 10.sp, color = Color.Gray)
                    Text(order.tanggal, fontSize = 12.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total: Rp ${order.total}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onDetail,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = "Detail", tint = Color.Blue, modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

