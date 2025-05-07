// app/src/main/java/com/example/sistemagestionbiblioteca/features/shelves/ShelvesViewModel.kt
package com.example.sistemagestionbiblioteca.features.shelves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.books.BookResponse
import com.example.sistemagestionbiblioteca.data.shelves.Shelf
import com.example.sistemagestionbiblioteca.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ShelvesViewModel(
    private val api: ApiService
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _shelves = MutableStateFlow<List<Shelf>>(emptyList())
    val shelves: StateFlow<List<Shelf>> = _shelves

    private val _booksByShelf = MutableStateFlow<Map<Int, List<Book>>>(emptyMap())
    val booksByShelf: StateFlow<Map<Int, List<Book>>> = _booksByShelf

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            fetchShelvesAndBooks()
            _isLoading.value = false
        }
    }

    private suspend fun fetchShelvesAndBooks() {
        val shelvesResponse = api.getShelves()
        if (shelvesResponse.isSuccessful) {
            val list = shelvesResponse.body().orEmpty()
            _shelves.value = list
            _booksByShelf.value = emptyMap()
            list.forEach { shelf ->
                val resp = api.getBooksByShelf(shelf.id)
                val books = if (resp.isSuccessful) resp.body().orEmpty() else emptyList()
                _booksByShelf.update { current -> current + (shelf.id to books) }
            }
        } else {
            Log.e("ShelvesVM", "Error cargando estanterías: ${shelvesResponse.code()}")
        }
    }

    /**
     * Borra un libro en la API y actualiza localmente sin refrescar.
     */
    fun deleteBook(shelfId: Int, book: Book) {
        val call: Call<BookResponse> = api.deleteBook(book.ID)
        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    _booksByShelf.update { current ->
                        val updated = current.toMutableMap()
                        updated[shelfId] = updated[shelfId]
                            .orEmpty()
                            .filter { it.ID != book.ID }
                        updated
                    }
                } else {
                    Log.e("ShelvesVM", "Error borrando libro ${book.ID}: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Log.e("ShelvesVM", "Fallo al borrar libro ${book.ID}: ${t.localizedMessage}", t)
            }
        })
    }
}
