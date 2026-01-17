package com.example.projectpammysql.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.viewmodel.OrderUiState
import com.example.projectpammysql.viewmodel.OrderViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel
import com.itextpdf.layout.element.Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Int,
    viewModel: OrderViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit,
    navigateToInvoice: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.getOrderDetail(orderId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Order") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Text("‹", fontSize = 24.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is OrderUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is OrderUiState.SuccessDetail -> {
                val order = (uiState as OrderUiState.SuccessDetail).order
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Order Information", fontWeight = FontWeight.Bold)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("ID", color = Color.Gray, fontSize = 12.sp)
                                    Text("${order.orderId}", fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Toko", color = Color.Gray, fontSize = 12.sp)
                                    Text(order.namaToko?:"-", fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("User", color = Color.Gray, fontSize = 12.sp)
                                    Text(order.username ?: "-", fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Tanggal", color = Color.Gray, fontSize = 12.sp)
                                    Text(order.tanggal, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Status", color = Color.Gray, fontSize = 12.sp)
                                    Text(order.status, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { navigateToInvoice(order.orderId) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Lihat Invoice")
                        }
                    }

                    item {
                        Text("Items", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    items(order.items, key = { it.barangId ?: 0 }) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(text = item.namaBarang ?: "Barang Tidak Tersedia", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Qty: ${item.jumlah}", fontSize = 11.sp)
                                    Text(text = "Rp ${item.hargaSatuan}", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    item {
                        HorizontalDivider()
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Rp ${order.total}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                    }
                }
            }

            is OrderUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text((uiState as OrderUiState.Error).message, color = Color.Red)
                }
            }

            else -> {}
        }
    }
}

