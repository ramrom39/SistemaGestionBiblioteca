package com.example.sistemagestionbiblioteca.features.shelves
//

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sistemagestionbiblioteca.network.ApiService
/**
 * Factory para crear instancias de [ShelvesViewModel] con las dependencias necesarias.
 *
 * @property api Cliente de la API utilizado por el ViewModel para solicitar datos.
 */
class ShelvesViewModelFactory(
    private val api: ApiService
) : ViewModelProvider.Factory {
    /**
     * Crea una instancia de ViewModel del tipo especificado.
     *
     * @throws IllegalArgumentException si la clase de ViewModel solicitada no es soportada.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShelvesViewModel::class.java)) {
            return ShelvesViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
