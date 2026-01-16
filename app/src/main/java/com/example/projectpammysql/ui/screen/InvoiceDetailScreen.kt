package com.example.projectpammysql.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.data.models.InvoiceRequest
import com.example.projectpammysql.viewmodel.InvoiceUiState
import com.example.projectpammysql.viewmodel.InvoiceViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailScreen(
    orderId: Int,
    viewModel: InvoiceViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        viewModel.getInvoiceByOrderId(orderId)
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is InvoiceUiState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
                viewModel.resetState()
                if (state.message.contains("dibuat")) {
                    viewModel.getInvoiceByOrderId(orderId)
                }
            }
            is InvoiceUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
            }
            else -> {}
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Buat Invoice") },
            text = { Text("Buat invoice baru untuk order ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createInvoice(InvoiceRequest(orderId = orderId))
                        showCreateDialog = false
                    }
                ) {
                    Text("Buat")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Invoice") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Text("‹", fontSize = 24.sp)
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        when (uiState) {
            is InvoiceUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is InvoiceUiState.SuccessDetail -> {
                val invoice = (uiState as InvoiceUiState.SuccessDetail).invoice
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
                                Text("Invoice #${invoice.invoiceId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                HorizontalDivider()
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Toko", color = Color.Gray, fontSize = 12.sp)
                                    Text(invoice.namaToko, fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("User", color = Color.Gray, fontSize = 12.sp)
                                    Text(invoice.username ?: "-", fontWeight = FontWeight.Bold)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Tanggal", color = Color.Gray, fontSize = 12.sp)
                                    Text(invoice.tanggal, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Text("Items", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    items(invoice.items, key = { it.namaBarang }) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(item.namaBarang, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Qty: ${item.jumlah}", fontSize = 11.sp)
                                    Text("Rp ${item.hargaSatuan}", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    item {
                        HorizontalDivider()
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Rp ${invoice.total}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.generateInvoicePDF(invoice.invoiceId, context) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            enabled = (uiState !is InvoiceUiState.Loading)
                        ) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (uiState is InvoiceUiState.Loading) "Downloading..." else "Download PDF")
                        }
                    }
                }
            }

            is InvoiceUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Invoice Belum Tersedia",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Red
                        )
                        Text(
                            "Invoice untuk order ini belum dibuat.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Text("Buat Invoice Sekarang")
                        }
                        Button(
                            onClick = navigateBack,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Kembali")
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

