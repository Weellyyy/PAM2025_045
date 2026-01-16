package com.example.projectpammysql.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectpammysql.data.models.BarangRequest
import com.example.projectpammysql.utils.ImageUtils
import com.example.projectpammysql.viewmodel.BarangUiState
import com.example.projectpammysql.viewmodel.BarangViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangEditScreen(
    barangId: Int,
    viewModel: BarangViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var namaBarang by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var gambarUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var gambarBase64 by remember { mutableStateOf<String?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val file = ImageUtils.getFileFromUri(context, it)
            if (file != null) {
                gambarBase64 = ImageUtils.fileToBase64(file)
                gambarUrl = "data:image/jpeg;base64,${gambarBase64}"
            }
        }
    }

    LaunchedEffect(barangId) {
        viewModel.getBarangById(barangId)
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is BarangUiState.SuccessDetail -> {
                if (!isLoaded) {
                    val barang = (uiState as BarangUiState.SuccessDetail).barang
                    namaBarang = barang.namaBarang
                    stok = barang.stok.toString()
                    harga = barang.harga
                    gambarUrl = barang.gambarUrl ?: ""
                    isLoaded = true
                }
            }
            is BarangUiState.Success -> {
                viewModel.resetState()
                navigateBack()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Barang") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState) {
                is BarangUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is BarangUiState.Error -> {
                    Text(
                        text = (uiState as BarangUiState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    OutlinedTextField(
                        value = namaBarang,
                        onValueChange = { namaBarang = it },
                        label = { Text("Nama Barang") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = stok,
                        onValueChange = { stok = it },
                        label = { Text("Stok") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = harga,
                        onValueChange = { harga = it },
                        label = { Text("Harga") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Image Selection Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .border(2.dp, Color.Gray)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (!gambarUrl.isNullOrEmpty() && !gambarUrl.startsWith("data:")) {
                            AsyncImage(
                                model = gambarUrl,
                                contentDescription = "Current Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Image,
                                    contentDescription = "Select Image",
                                    modifier = Modifier.padding(8.dp),
                                    tint = Color.Gray
                                )
                                Text("Tap untuk ubah gambar", color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            val barangRequest = BarangRequest(
                                namaBarang = namaBarang,
                                stok = stok.toIntOrNull() ?: 0,
                                harga = harga.toDoubleOrNull() ?: 0.0,
                                gambarUrl = if (gambarBase64 != null) null else gambarUrl.ifEmpty { null },
                                gambarBase64 = gambarBase64
                            )
                            viewModel.updateBarang(barangId, barangRequest)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        enabled = namaBarang.isNotEmpty() && stok.isNotEmpty() && harga.isNotEmpty()
                    ) {
                        Text("Simpan Perubahan")
                    }
                }
            }
        }
    }
}

