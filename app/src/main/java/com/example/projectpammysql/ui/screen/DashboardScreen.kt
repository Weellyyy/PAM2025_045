package com.example.projectpammysql.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectpammysql.ui.components.BottomNavigationDashboard
import com.example.projectpammysql.viewmodel.DashboardViewModel
import com.example.projectpammysql.viewmodel.provider.PenyediaViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory),
    navigateToBarang: () -> Unit,
    navigateToToko: () -> Unit,
    navigateToOrder: () -> Unit,
    navigateLogout: () -> Unit,
    navigateToDashboard: () -> Unit = {}
) {
    val jumlahBarang by viewModel.jumlahBarang.collectAsState()
    val jumlahToko by viewModel.jumlahToko.collectAsState()
    val jumlahOrder by viewModel.jumlahOrder.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = navigateLogout) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
            } else {
                // Jumlah Barang Card
                DashboardCard(
                    title = "Jumlah Barang",
                    value = jumlahBarang.toString()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Jumlah Toko Card
                DashboardCard(
                    title = "Jumlah Toko",
                    value = jumlahToko.toString()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Jumlah Order Card
                DashboardCard(
                    title = "Jumlah Order",
                    value = jumlahOrder.toString()
                )
            }
        }

        // Bottom Navigation
        BottomNavigationDashboard(
            navigateToBarang = navigateToBarang,
            navigateToToko = navigateToToko,
            navigateToOrder = navigateToOrder,
            navigateToDashboard = navigateToDashboard,
            currentRoute = "dashboard"
        )
    }
}

@Composable
fun DashboardCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

