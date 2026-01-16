package com.example.projectpammysql.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.projectpammysql.data.models.BarangRequest
import com.example.projectpammysql.utils.ImageUtils
import com.example.projectpammysql.viewmodel.BarangUiState
import com.example.projectpammysql.viewmodel.BarangViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangEntryScreen(
    token: String,
    viewModel: BarangViewModel = viewModel(factory = PenyediaViewModel.Factory),
    NavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(token) {
        println("DEBUG - Token yang diterima di BarangEntryScreen: $token")
        if (token.isEmpty()) {
            println("ERROR - Token kosong!")
        }
    }

    var namaBarang by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var gambarUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var gambarBase64 by remember { mutableStateOf<String?>(null) }

    val uiState = viewModel.uiState.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Convert to Base64
            val file = ImageUtils.getFileFromUri(context, it)
            if (file != null) {
                gambarBase64 = ImageUtils.fileToBase64(file)
                gambarUrl = "data:image/jpeg;base64,${gambarBase64}"
            }
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is BarangUiState.Success -> {
                scope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
                NavigateBack()
            }
            is BarangUiState.Error -> {
                scope.launch {
                    snackbarHostState.showSnackbar(uiState.message)
                }
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Barang") },
                navigationIcon = {
                    IconButton(onClick = NavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = namaBarang,
                onValueChange = { namaBarang = it },
                label = { Text("Nama Barang") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = stok,
                onValueChange = { stok = it },
                label = { Text("Stok") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = harga,
                onValueChange = { harga = it },
                label = { Text("Harga") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
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
                        Text("Tap untuk pilih gambar", color = Color.Gray)
                    }
                }
            }

            Button(
                onClick = {
                    val barangRequest = BarangRequest(
                        namaBarang = namaBarang,
                        stok = stok.toIntOrNull() ?: 0,
                        harga = harga.toDoubleOrNull() ?: 0.0,
                        gambarUrl = if (gambarBase64 != null) null else gambarUrl.ifEmpty { null },
                        gambarBase64 = gambarBase64
                    )
                    println("DEBUG - Token sebelum dikirim: $token")
                    println("DEBUG - Token length: ${token.length}")
                    viewModel.createBarang(barangRequest)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = uiState !is BarangUiState.Loading &&
                        namaBarang.isNotEmpty() &&
                        stok.isNotEmpty() &&
                        harga.isNotEmpty()
            ) {
                if (uiState is BarangUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                }
                Text("Simpan")
            }
        }
    }
}
