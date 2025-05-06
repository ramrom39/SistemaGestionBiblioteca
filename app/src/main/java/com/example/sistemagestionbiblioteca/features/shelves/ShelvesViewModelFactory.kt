package com.example.sistemagestionbiblioteca.features.shelves
//

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sistemagestionbiblioteca.network.ApiService

class ShelvesViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShelvesViewModel::class.java)) {
            return ShelvesViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
