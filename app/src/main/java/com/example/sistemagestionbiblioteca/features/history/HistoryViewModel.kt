package com.example.sistemagestionbiblioteca.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.categories.Category
import com.example.sistemagestionbiblioteca.data.history.History
import com.example.sistemagestionbiblioteca.data.users.UserNameResponse
import com.example.sistemagestionbiblioteca.network.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    var searchQuery = MutableStateFlow("")
        private set

    private val _suggestions  = MutableStateFlow<List<Book>>(emptyList())
    val suggestions: StateFlow<List<Book>> = _suggestions

    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook

    private val _categoryName = MutableStateFlow("")
    val categoryName: StateFlow<String> = _categoryName

    private val _historyList  = MutableStateFlow<List<History>>(emptyList())
    val historyList: StateFlow<List<History>> = _historyList

    private val _userNames    = MutableStateFlow<Map<Int, String>>(emptyMap())
    val userNames: StateFlow<Map<Int, String>> = _userNames

    private val api = ApiService.getInstance()

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

    fun onBookSelected(book: Book) {
        // Exactamente el mismo flujo que onSearchButtonClicked
        searchQuery.value    = book.Título
        _suggestions.value   = emptyList()
        _selectedBook.value  = book
        fetchCategoryName(book.Categoría_ID)
        loadHistoryAndUserNames(book.ID)
    }

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

    private fun clearAllData() {
        _historyList.value   = emptyList()
        _categoryName.value  = ""
        _userNames.value     = emptyMap()
    }

    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}
