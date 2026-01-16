package com.example.projectpammysql.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectpammysql.ui.uicontroller.route.DestinasiBarang
import com.example.projectpammysql.ui.uicontroller.route.DestinasiBarangEdit
import com.example.projectpammysql.ui.uicontroller.route.DestinasiBarangEntry
import com.example.projectpammysql.ui.screen.BarangEditScreen
import com.example.projectpammysql.ui.screen.BarangEntryScreen
import com.example.projectpammysql.ui.screen.BarangListScreen
import com.example.projectpammysql.ui.screen.DashboardScreen
import com.example.projectpammysql.ui.screen.InvoiceDetailScreen
import com.example.projectpammysql.ui.screen.LoginScreen
import com.example.projectpammysql.ui.screen.OrderAddScreen
import com.example.projectpammysql.ui.screen.OrderDetailScreen
import com.example.projectpammysql.ui.screen.OrderListScreen
import com.example.projectpammysql.ui.screen.TokoEditScreen
import com.example.projectpammysql.ui.screen.TokoEntryScreen
import com.example.projectpammysql.ui.screen.TokoListScreen
import com.example.projectpammysql.ui.uicontroller.route.DestinasiDashboard
import com.example.projectpammysql.ui.uicontroller.route.DestinasiInvoiceDetail
import com.example.projectpammysql.ui.uicontroller.route.DestinasiLogin
import com.example.projectpammysql.ui.uicontroller.route.DestinasiOrder
import com.example.projectpammysql.ui.uicontroller.route.DestinasiOrderDetail
import com.example.projectpammysql.ui.uicontroller.route.DestinasiOrderEntry
import com.example.projectpammysql.ui.uicontroller.route.DestinasiToko
import com.example.projectpammysql.ui.uicontroller.route.DestinasiTokoEdit
import com.example.projectpammysql.ui.uicontroller.route.DestinasiTokoEntry

@Composable
fun PamMyAppsApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    token: String
) {
    HostNavigasi(
        navController = navController,
        modifier = modifier,
        token = token
    )
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    token: String
) {

    NavHost(
        navController = navController,
        startDestination = DestinasiLogin.route,
        modifier = modifier
    ) {
        // ============ LOGIN ============
        composable(DestinasiLogin.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(DestinasiDashboard.route) {
                        popUpTo(DestinasiLogin.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        // ============ DASHBOARD ============
        composable(DestinasiDashboard.route) {
            DashboardScreen(
                navigateToBarang = {
                    navController.navigate(DestinasiBarang.route)
                },
                navigateToToko = {
                    navController.navigate(DestinasiToko.route)
                },
                navigateToOrder = {
                    navController.navigate(DestinasiOrder.route)
                },
                navigateLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiDashboard.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ============ BARANG ROUTES ============
        composable(DestinasiBarang.route) {
            BarangListScreen(
                NavigateBack = {
                    navController.navigate(DestinasiDashboard.route)
                },
                NavigateToAdd = {
                    navController.navigate(DestinasiBarangEntry.route)
                },
                NavigateToEdit = {
                    navController.navigate("${DestinasiBarangEdit.route}/$it")
                },
                navigateToBarang = {
                    navController.navigate(DestinasiBarang.route)
                },
                navigateToToko = {
                    navController.navigate(DestinasiToko.route)
                },
                navigateToOrder = {
                    navController.navigate(DestinasiOrder.route)
                },
                navigateToDashboard = {
                    navController.navigate(DestinasiDashboard.route)
                },
                navigateLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiDashboard.route) {
                            inclusive = true
                        }
                    }
                },
                token = token
            )
        }

        composable(DestinasiBarangEntry.route) {
            BarangEntryScreen(
                NavigateBack = {
                    navController.navigate(DestinasiBarang.route)
                },
                token = token
            )
        }

        composable(
            route = DestinasiBarangEdit.routeWithArgs,
            arguments = listOf(navArgument(DestinasiBarangEdit.barangId) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val barangId = backStackEntry.arguments?.getInt(DestinasiBarangEdit.barangId) ?: 0
            BarangEditScreen(
                barangId = barangId,
                navigateBack = {
                    navController.navigate(DestinasiBarang.route) {
                        popUpTo(DestinasiBarang.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        // ============ TOKO ROUTES ============
        composable(DestinasiToko.route) {
            TokoListScreen(
                NavigateBack = {
                    navController.navigate(DestinasiDashboard.route)
                },
                NavigateToAdd = {
                    navController.navigate(DestinasiTokoEntry.route)
                },
                NavigateToEdit = {
                    navController.navigate("${DestinasiTokoEdit.route}/$it")
                },
                navigateToBarang = {
                    navController.navigate(DestinasiBarang.route)
                },
                navigateToToko = {
                    navController.navigate(DestinasiToko.route)
                },
                navigateToOrder = {
                    navController.navigate(DestinasiOrder.route)
                },
                navigateToDashboard = {
                    navController.navigate(DestinasiDashboard.route)
                },
                navigateLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiDashboard.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(DestinasiTokoEntry.route) {
            TokoEntryScreen(
                NavigateBack = {
                    navController.navigate(DestinasiToko.route)
                }
            )
        }

        composable(
            route = DestinasiTokoEdit.routeWithArgs,
            arguments = listOf(navArgument(DestinasiTokoEdit.tokoId) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val tokoId = backStackEntry.arguments?.getInt(DestinasiTokoEdit.tokoId) ?: 0
            TokoEditScreen(
                tokoId = tokoId,
                navigateBack = {
                    navController.navigate(DestinasiToko.route) {
                        popUpTo(DestinasiToko.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        // ============ ORDER ROUTES ============
        composable(DestinasiOrder.route) {
            OrderListScreen(
                NavigateBack = {
                    navController.navigate(DestinasiDashboard.route)
                },
                NavigateToAdd = {
                    navController.navigate(DestinasiOrderEntry.route)
                },
                NavigateToDetail = {
                    navController.navigate("${DestinasiOrderDetail.route}/$it")
                },
                navigateToBarang = {
                    navController.navigate(DestinasiBarang.route)
                },
                navigateToToko = {
                    navController.navigate(DestinasiToko.route)
                },
                navigateToOrder = {
                    navController.navigate(DestinasiOrder.route)
                },
                navigateToDashboard = {
                    navController.navigate(DestinasiDashboard.route)
                },
                navigateLogout = {
                    navController.navigate(DestinasiLogin.route) {
                        popUpTo(DestinasiDashboard.route) {
                            inclusive = true
                        }
                    }
                },
            )
        }

        composable(DestinasiOrderEntry.route) {
            val userId = 1 // TODO: Get from TokenManager or session
            OrderAddScreen(
                userId = userId,
                navigateBack = {
                    navController.navigate(DestinasiOrder.route) {
                        popUpTo(DestinasiOrder.route) {
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(
            route = DestinasiOrderDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiOrderDetail.orderId) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt(DestinasiOrderDetail.orderId) ?: 0
            OrderDetailScreen(
                orderId = orderId,
                navigateBack = {
                    navController.navigate(DestinasiOrder.route)
                },
                navigateToInvoice = {
                    navController.navigate("${DestinasiInvoiceDetail.route}/$it")
                }
            )
        }

        // ============ INVOICE ROUTES ============
        composable(
            route = DestinasiInvoiceDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiInvoiceDetail.orderId) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt(DestinasiInvoiceDetail.orderId) ?: 0
            InvoiceDetailScreen(
                orderId = orderId,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

    }
}
