// features/shelves/ShelvesViewModel.kt
package com.example.sistemagestionbiblioteca.features.shelves

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.shelves.Shelf
import com.example.sistemagestionbiblioteca.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ShelvesViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _shelves = MutableStateFlow<List<Shelf>>(emptyList())
    val shelves: StateFlow<List<Shelf>> = _shelves

    private val _booksByShelf = MutableStateFlow<Map<Int, List<Book>>>(emptyMap())
    val booksByShelf: StateFlow<Map<Int, List<Book>>> = _booksByShelf

    init {
        viewModelScope.launch { fetchShelvesAndBooks() }
    }

    private suspend fun fetchShelvesAndBooks() {
        // 1) Traer todas las estanterías
        api.getShelves().takeIf { it.isSuccessful }?.body().orEmpty().let { list ->
            _shelves.value = list
            // 2) Para cada estantería, traer sus libros mediante query ?shelfId=
            list.forEach { shelf ->
                api.getBooksByShelf(shelf.id)
                    .takeIf { it.isSuccessful }
                    ?.body()
                    .orEmpty()
                    .also { books ->
                        // Guarda en el map[id] = books
                        _booksByShelf.value = _booksByShelf.value + (shelf.id to books)
                    }
            }
        }
    }

    fun deleteBook(shelfId: Int, book: Book) {
        viewModelScope.launch {
            try {
                // Ejecutamos el Call en IO para bloquear hasta la respuesta
                val call = api.deleteBook(book.ID)
                val response = withContext(Dispatchers.IO) {
                    call.execute()       // devuelve retrofit2.Response<BookResponse>
                }
                if (response.isSuccessful) {
                    // Actualizar estado local filtrando el libro borrado
                    val updated = _booksByShelf.value.toMutableMap()
                    updated[shelfId] = updated[shelfId]
                        .orEmpty()
                        .filter { it.ID != book.ID }
                    _booksByShelf.value = updated
                } else {
                    Log.e("ShelvesVM", "Error borrando libro ${book.ID}: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ShelvesVM", "Excepción borrando libro ${book.ID}", e)
            }
        }
    }
}
