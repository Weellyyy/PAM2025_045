package com.example.projectpammysql.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.data.models.BarangResponse
import com.example.projectpammysql.data.models.OrderItemForm
import com.example.projectpammysql.data.models.OrderRequest
import com.example.projectpammysql.data.models.TokoResponse
import com.example.projectpammysql.viewmodel.BarangViewModel
import com.example.projectpammysql.viewmodel.BarangUiState
import com.example.projectpammysql.viewmodel.TokoViewModel
import com.example.projectpammysql.viewmodel.TokoUiState
import com.example.projectpammysql.viewmodel.OrderViewModel
import com.example.projectpammysql.viewmodel.OrderUiState
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel
import com.example.projectpammysql.utils.StokValidator
import kotlinx.coroutines.launch




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderAddScreen(
    userId: Int,
    barangViewModel: BarangViewModel = viewModel(factory = PenyediaViewModel.Factory),
    tokoViewModel: TokoViewModel = viewModel(factory = PenyediaViewModel.Factory),
    orderViewModel: OrderViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val barangUiState by barangViewModel.uiState.collectAsState()
    val tokoUiState by tokoViewModel.uiState.collectAsState()
    val orderUiState by orderViewModel.uiState.collectAsState()

    val barangList = (barangUiState as? BarangUiState.SuccessList)?.barangList ?: emptyList()
    val tokoList = (tokoUiState as? TokoUiState.SuccessList)?.tokoList ?: emptyList()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var hasHandledResult by remember { mutableStateOf(false) }


    var selectedToko by remember { mutableStateOf<TokoResponse?>(null) }
    var selectedStatus by remember { mutableStateOf("pending") }
    var orderItems by remember { mutableStateOf<List<OrderItemForm>>(emptyList()) }
    var expandedTokoDropdown by remember { mutableStateOf(false) }
    //var expandedStatusDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        barangViewModel.getAllBarang()
        tokoViewModel.getAllToko()
    }

    LaunchedEffect(orderUiState) {
        if (hasHandledResult) return@LaunchedEffect

        when (orderUiState) {
            is OrderUiState.Success -> {
                hasHandledResult = true
                snackbarHostState.showSnackbar("Order berhasil dibuat")
                orderViewModel.resetState()
                navigateBack()
            }

            is OrderUiState.Error -> {
                hasHandledResult = true
                snackbarHostState.showSnackbar(
                    (orderUiState as OrderUiState.Error).message
                )
                orderViewModel.resetState()
            }

            else -> {}
        }
    }


    val totalOrder = orderItems.sumOf { it.getSubtotal() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Order") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.Close, contentDescription = "Kembali")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pilih Toko
            item {
                Text("Pilih Toko", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Box {
                    OutlinedButton(
                        onClick = { expandedTokoDropdown = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedToko?.namaToko ?: "Pilih Toko")
                    }
                    DropdownMenu(
                        expanded = expandedTokoDropdown,
                        onDismissRequest = { expandedTokoDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        tokoList.forEach { toko ->
                            DropdownMenuItem(
                                text = { Text(toko.namaToko) },
                                onClick = {
                                    selectedToko = toko
                                    expandedTokoDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // User ID
            item {
                Text("User ID", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                OutlinedTextField(
                    value = userId.toString(),
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
            }

            // Pilih Status


            // Items Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Items", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Button(
                        onClick = {
                            orderItems = orderItems + OrderItemForm()
                        },
                        modifier = Modifier.height(36.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tambah Item", fontSize = 12.sp)
                    }
                }
            }

            // Order Items List
            items(orderItems.size, key = { index -> "item_$index" }) { index ->
                OrderItemFormCard(
                    item = orderItems[index],
                    barangList = barangList,
                    index = index,
                    onUpdate = { updatedItem ->
                        orderItems = orderItems.toMutableList().apply {
                            set(index, updatedItem)
                        }
                    },
                    onDelete = {
                        orderItems = orderItems.filterIndexed { i, _ -> i != index }
                    }
                )
            }

            // Total Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Order", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Rp ${String.format("%.2f", totalOrder)}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                    }
                }
            }

            // Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = navigateBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            if (selectedToko == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Pilih toko terlebih dahulu")
                                }
                                return@Button
                            }
                            if (orderItems.isEmpty()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Tambahkan minimal 1 item")
                                }
                                return@Button
                            }

                            val stokError = StokValidator.validateStok(orderItems, barangList)
                            if (stokError != null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(stokError)
                                }
                                return@Button
                            }

                            val request = OrderRequest(
                                tokoId = selectedToko!!.tokoId,
                                userId = userId,
                                status = selectedStatus,
                                items = orderItems.map { it.toOrderItem() }
                            )
                            orderViewModel.createOrder(request)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        enabled = orderUiState !is OrderUiState.Loading
                    ) {
                        Text(if (orderUiState is OrderUiState.Loading) "Menyimpan..." else "Simpan Order")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemFormCard(
    item: OrderItemForm,
    barangList: List<BarangResponse>,
    onUpdate: (OrderItemForm) -> Unit,
    onDelete: () -> Unit,
    index: Int = 0,
    modifier: Modifier = Modifier
) {
    var expandedBarangDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Item
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(
                    modifier = Modifier.background(
                        color = Color(0xFF1976D2),
                        shape = RoundedCornerShape(50.dp)
                    )
                ) {
                    Text(
                        "Item ${index + 1}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(6.dp, 3.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Hapus",
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)

            // Pilih Barang
            Text("Barang", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray)
            Box {
                OutlinedButton(
                    onClick = { expandedBarangDropdown = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        item.namaBarang.ifEmpty { "Pilih Barang..." },
                        modifier = Modifier.weight(1f),
                        color = if (item.namaBarang.isEmpty()) Color.Gray else Color.Black
                    )
                }
                DropdownMenu(
                    expanded = expandedBarangDropdown,
                    onDismissRequest = { expandedBarangDropdown = false },
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) {
                    barangList.forEach { barang ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(barang.namaBarang, fontWeight = FontWeight.SemiBold)
                                    Text("Stok: ${barang.stok} | Rp ${String.format("%.0f", barang.harga.toDoubleOrNull() ?: 0.0)}", fontSize = 11.sp, color = Color.Gray)
                                }
                            },
                            onClick = {
                                onUpdate(item.copy(
                                    barangId = barang.barangId,
                                    namaBarang = barang.namaBarang,
                                    hargaSatuan = barang.harga.toDoubleOrNull() ?: 0.0
                                ))
                                expandedBarangDropdown = false
                            }
                        )
                    }
                }
            }

            // Qty dan Harga
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(0.4f)) {
                    Text("Qty", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    OutlinedTextField(
                        value = item.jumlah.toString(),
                        onValueChange = { newValue ->
                            val jumlah = newValue.toIntOrNull() ?: 1
                            onUpdate(item.copy(jumlah = jumlah.coerceAtLeast(1)))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(6.dp)
                    )
                }

                Column(modifier = Modifier.weight(0.6f)) {
                    Text("Harga Satuan", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Text(
                            "Rp ${String.format("%.0f", item.hargaSatuan)}",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                                .wrapContentHeight(Alignment.CenterVertically),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Color(0xFF1976D2)
                        )
                    }
                }
            }

            // Stock Status
            val barangItem = barangList.find { it.barangId == item.barangId }
            if (barangItem != null) {
                val stokTersedia = barangItem.stok
                val isStokKurang = stokTersedia < item.jumlah

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isStokKurang) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isStokKurang) "⚠️" else "✅", fontSize = 16.sp)
                        Text(
                            if (isStokKurang) "Stok tidak cukup! Tersedia: $stokTersedia" else "Stok tersedia: $stokTersedia",
                            fontSize = 12.sp,
                            color = if (isStokKurang) Color(0xFFC62828) else Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Subtotal
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Subtotal:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.Gray)
                    Text("Rp ${String.format("%.0f", item.getSubtotal())}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1976D2))
                }
            }
        }
    }
}

