package com.example.projectpammysql.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationDashboard(
    navigateToBarang: () -> Unit,
    navigateToToko: () -> Unit,
    navigateToOrder: () -> Unit,
    navigateToDashboard: () -> Unit = {},
    currentRoute: String = "barang"
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        containerColor = Color.White
    ) {

        // Dashboard
        NavigationBarItem(
            selected = currentRoute == "dashboard",
            onClick = navigateToDashboard,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Dashboard",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("DASHBOARD", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Blue,
                unselectedTextColor = Color.Gray
            )
        )

        // Barang
        NavigationBarItem(
            selected = currentRoute == "barang",
            onClick = navigateToBarang,
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Barang",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("BARANG", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Blue,
                unselectedTextColor = Color.Gray
            )
        )

        // Toko
        NavigationBarItem(
            selected = currentRoute == "toko",
            onClick = navigateToToko,
            icon = {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Toko",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("TOKO", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Blue,
                unselectedTextColor = Color.Gray
            )
        )

        // Transaksi
        NavigationBarItem(
            selected = currentRoute == "order",
            onClick = navigateToOrder,
            icon = {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Transaksi",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text("TRANSAKSI", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.Blue,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
