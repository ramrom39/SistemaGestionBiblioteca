package com.example.sistemagestionbiblioteca.features.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.books.BookResponse
import com.example.sistemagestionbiblioteca.data.books.BookUpdateRequest
import com.example.sistemagestionbiblioteca.data.categories.Category
import com.example.sistemagestionbiblioteca.data.history.History
import com.example.sistemagestionbiblioteca.data.users.UserNameResponse
import com.example.sistemagestionbiblioteca.network.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
/**
 * ViewModel para gestionar la búsqueda de libros, la selección de un libro,
 * la obtención de su historial de acciones y los nombres de usuario asociados.
 *
 * Expone flujos para la consulta, sugerencias, datos seleccionados y eventos UI.
 */
class HistoryViewModel : ViewModel() {
    /**
     * Cadena de búsqueda actual. Al cambiar, reinicia selección y datos relacionados.
     */
    var searchQuery = MutableStateFlow("")
        private set

    private val _uiEvents = MutableSharedFlow<String>()
    /**
     * Flujo de eventos de UI (mensajes de éxito/error) para la vista.
     */
    val uiEvents = _uiEvents.asSharedFlow()

    private val _suggestions  = MutableStateFlow<List<Book>>(emptyList())
    /**
     * Sugerencias de libros basadas en la búsqueda parcial.
     */
    val suggestions: StateFlow<List<Book>> = _suggestions

    private val _selectedBook = MutableStateFlow<Book?>(null)
    /**
     * Libro seleccionado tras búsqueda exacta.
     */
    val selectedBook: StateFlow<Book?> = _selectedBook

    private val _categoryName = MutableStateFlow("")
    /**
     * Nombre de la categoría del libro seleccionado.
     */
    val categoryName: StateFlow<String> = _categoryName

    private val _historyList  = MutableStateFlow<List<History>>(emptyList())
    /**
     * Historial de acciones (préstamos, devoluciones) del libro seleccionado.
     */
    val historyList: StateFlow<List<History>> = _historyList

    private val _userNames    = MutableStateFlow<Map<Int, String>>(emptyMap())
    /**
     * Mapa de IDs de usuario a nombres, utilizado para mostrar quién realizó cada acción.
     */
    val userNames: StateFlow<Map<Int, String>> = _userNames

    private val api = ApiService.getInstance()
    /**
     * Actualiza la consulta de búsqueda y obtiene sugerencias filtradas
     * por título de libro que contenga la cadena.
     *
     * @param query Texto ingresado por el usuario.
     */
    fun onSearchQueryChanged(query: String) {
        searchQuery.value    = query
        _selectedBook.value  = null
        _historyList.value   = emptyList()
        _categoryName.value  = ""
        _userNames.value     = emptyMap()

        if (query.isBlank()) {
            _suggestions.value = emptyList()
            return
        }
        api.getBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                _suggestions.value = response.body().orEmpty()
                    .filter { it.Título.contains(query, ignoreCase = true) }
            }
            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                _suggestions.value = emptyList()
            }
        })
    }
    /**
     * Ejecuta la búsqueda exacta de libro y, si se encuentra,
     * carga su categoría y su historial con nombres de usuario.
     */
    fun onSearchButtonClicked() {
        api.getBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                val books = response.body().orEmpty()
                val book  = books.firstOrNull {
                    it.Título.equals(searchQuery.value, ignoreCase = true)
                }
                _selectedBook.value = book
                _suggestions.value  = emptyList()

                if (book != null) {
                    fetchCategoryName(book.Categoría_ID)
                    loadHistoryAndUserNames(book.ID)
                } else {
                    clearAllData()
                }
            }
            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                clearAllData()
            }
        })
    }
    /**
     * Selecciona un libro de la lista de sugerencias y carga
     * su categoría e historial de acciones.
     *
     * @param book Libro elegido por el usuario.
     */
    fun onBookSelected(book: Book) {
        // Exactamente el mismo flujo que onSearchButtonClicked
        searchQuery.value    = book.Título
        _suggestions.value   = emptyList()
        _selectedBook.value  = book
        fetchCategoryName(book.Categoría_ID)
        loadHistoryAndUserNames(book.ID)
    }
    /**
     * Carga en paralelo el historial de un libro y obtiene los nombres
     * de usuario que realizaron cada acción.
     *
     * @param bookId ID del libro cuyo historial se quiere recuperar.
     */
    private fun loadHistoryAndUserNames(bookId: Int) {
        viewModelScope.launch {
            val history = try {
                api.getHistoryByBookId(bookId)
            } catch (_: Exception) {
                emptyList()
            }
            _historyList.value = history

            // Disparamos todas las llamadas en paralelo
            val map = coroutineScope {
                history.map { h ->
                    async {
                        h.userId to runCatching { api.getUserById(h.userId) }
                            .getOrNull()?.nombre
                            .orEmpty()
                    }
                }.awaitAll().toMap()
            }
            _userNames.value = map
        }
    }
    /**
     * Recupera el nombre de la categoría dada su ID.
     *
     * @param categoryId ID de la categoría a buscar.
     */
    private fun fetchCategoryName(categoryId: Int) {
        api.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                _categoryName.value = response.body().orEmpty()
                    .firstOrNull { it.id == categoryId }
                    ?.nombre
                    .orEmpty()
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                _categoryName.value = ""
            }
        })
    }
    /**
     * Envía una petición para actualizar los datos de un libro y,
     * al confirmar éxito, recarga su detalle e historial y emite un evento.
     *
     * @param book Objeto con datos actuales del libro.
     * @param userId ID del usuario que realiza la actualización.
     */
    fun updateBook(book: Book, userId: Int) {
        val req = BookUpdateRequest(
            titulo             = book.Título,
            autor              = book.Autor,
            año                = book.Año,
            sinopsis           = book.Sinopsis,
            categoria          = book.Categoría_ID,
            estado             = book.Estado,
            fecha              = book.Fecha,
            estanteria         = book.Estanteria_ID,
            usuarioModificador = userId
        )

        ApiService.getInstance()
            .updateBook(book.ID, req)
            .enqueue(object : Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        // refresca detalle/historial…
                        onBookSelected(book)
                        // 2. Emite evento de éxito
                        viewModelScope.launch {
                            _uiEvents.emit("Libro actualizado correctamente")
                        }
                    } else {
                        // 3. Emite evento de error
                        viewModelScope.launch {
                            _uiEvents.emit("Error al actualizar libro")
                        }
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    viewModelScope.launch {
                        _uiEvents.emit("Fallo de red: ${t.localizedMessage}")
                    }
                }
            })
    }
    /**
     * Limpia todos los datos (historial, categoría y nombres) de la vista.
     */
    private fun clearAllData() {
        _historyList.value   = emptyList()
        _categoryName.value  = ""
        _userNames.value     = emptyMap()
    }
    /**
     * Limpia las sugerencias de búsqueda.
     */
    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}
