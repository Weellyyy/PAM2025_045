package com.example.projectpammysql.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectpammysql.data.repositories.AplikasiDataApp
import com.example.projectpammysql.viewmodel.AuthViewModel
import com.example.projectpammysql.viewmodel.BarangViewModel
import com.example.projectpammysql.viewmodel.DashboardViewModel
import com.example.projectpammysql.viewmodel.InvoiceViewModel
import com.example.projectpammysql.viewmodel.OrderDetailViewModel
import com.example.projectpammysql.viewmodel.OrderViewModel
import com.example.projectpammysql.viewmodel.TokoViewModel


fun CreationExtras.aplikasiDataApp(): AplikasiDataApp = (
        this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                AplikasiDataApp
        )

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { AuthViewModel(aplikasiDataApp().container.repositoriAuth,aplikasiDataApp().container.tokenManager) }
        initializer { TokoViewModel(aplikasiDataApp().container.repositoriToko, aplikasiDataApp().container.tokenManager) }
        initializer { BarangViewModel(aplikasiDataApp().container.repositoriBarang,aplikasiDataApp().container.tokenManager) }
        initializer { OrderViewModel(aplikasiDataApp().container.repositoriOrder) }
        initializer { OrderDetailViewModel(aplikasiDataApp().container.repositoriOrderDetail) }
        initializer { InvoiceViewModel(aplikasiDataApp().container.repositoriInvoice) }
        initializer {
            DashboardViewModel(
                aplikasiDataApp().container.repositoriBarang,
                aplikasiDataApp().container.repositoriToko,
                aplikasiDataApp().container.repositoriOrder,
                aplikasiDataApp().container.tokenManager
            )
        }
    }
}

