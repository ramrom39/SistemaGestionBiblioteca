package com.example.sistemagestionbiblioteca.features.books


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.books.BookCreateRequest
import com.example.sistemagestionbiblioteca.data.books.BookResponse
import com.example.sistemagestionbiblioteca.data.books.BookUpdateRequest
import com.example.sistemagestionbiblioteca.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel encargado de gestionar las operaciones CRUD de libros y el estado de la UI.
 *
 * @property books Lista de libros obtenida de la API como LiveData.
 * @property statusMessage Mensaje de estado o error para mostrar en la UI.
 */
class BookViewModel : ViewModel() {
    private val _books = MutableLiveData<List<Book>>()

    /**
     * LiveData público con la lista de libros.
     */
    val books: LiveData<List<Book>> = _books

    private val _statusMessage = MutableLiveData<String?>()

    /**
     * LiveData público con mensajes de estado o error.
     */
    val statusMessage: LiveData<String?> = _statusMessage

    /**
     * Recupera todos los libros desde la API y actualiza `books` o `statusMessage`.
     */
    fun fetchBooks() {
        ApiService.getInstance().getBooks().enqueue(object: Callback<List<Book>> {
            override fun onResponse(c: Call<List<Book>>, r: Response<List<Book>>) {
                if (r.isSuccessful) _books.postValue(r.body()!!)
                else _statusMessage.postValue("Error ${r.code()} al cargar libros")
            }
            override fun onFailure(c: Call<List<Book>>, t: Throwable) {
                _statusMessage.postValue("Error de red al cargar")
            }
        })
    }
    /**
     * Envía una petición para crear un nuevo libro y refresca la lista en caso de éxito.
     *
     * @param r Datos para crear el libro.
     */
    fun createBook(r: BookCreateRequest) {
        ApiService.getInstance().createBook(r)
            .enqueue(object: Callback<BookResponse> {
                override fun onResponse(c: Call<BookResponse>, r: Response<BookResponse>) {
                    if (r.isSuccessful && r.body() != null) {
                        _statusMessage.postValue("${r.body()!!.message} (ID=${r.body()!!.id})")
                        fetchBooks()
                    } else {
                        _statusMessage.postValue("Error ${r.code()} al crear")
                    }
                }
                override fun onFailure(c: Call<BookResponse>, t: Throwable) {
                    _statusMessage.postValue("Error de red al crear")
                }
            })
    }
    /**
     * Envía una petición para actualizar un libro existente y refresca la lista en caso de éxito.
     *
     * @param book Libro con datos actuales.
     * @param userId ID del usuario que realiza la modificación.
     */
    fun updateBook(book: Book, userId: Int) {
        val req = BookUpdateRequest(
            titulo              = book.Título,
            autor               = book.Autor,
            año               = book.Año,
            sinopsis            = book.Sinopsis,
            categoria           = book.Categoría_ID,
            estado              = book.Estado,
            fecha               = book.Fecha,
            estanteria          = book.Estanteria_ID,
            usuarioModificador  = userId
        )

        ApiService.getInstance()
            .updateBook(book.ID, req)
            .enqueue(object : Callback<BookResponse> {
                override fun onResponse(call: Call<BookResponse>, resp: Response<BookResponse>) {
                    if (resp.isSuccessful && resp.body() != null) {
                        _statusMessage.postValue(resp.body()!!.message)
                        fetchBooks()
                    } else {
                        _statusMessage.postValue("Error ${resp.code()}: ${resp.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    _statusMessage.postValue("Error de red: ${t.localizedMessage}")
                }
            })
    }

    /**
     * Envía una petición para eliminar un libro por su ID y refresca la lista en caso de éxito.
     *
     * @param id ID del libro a eliminar.
     */
    fun deleteBook(id: Int) {
        ApiService.getInstance().deleteBook(id)
            .enqueue(object: Callback<BookResponse> {
                override fun onResponse(call: Call<BookResponse>, resp: Response<BookResponse>) {
                    if (resp.isSuccessful && resp.body()!=null) {
                        _statusMessage.postValue(resp.body()!!.message)
                        fetchBooks()
                    } else {
                        _statusMessage.postValue("Error ${resp.code()}: ${resp.errorBody()?.string()}")
                    }
                }
                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    _statusMessage.postValue("Error de red al borrar: ${t.localizedMessage}")
                }
            })
    }
    /**
     * Limpia el mensaje de estado para no mostrar nada.
     */
    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
