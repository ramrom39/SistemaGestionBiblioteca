package com.example.sistemagestionbiblioteca.features.category

import androidx.lifecycle.*
import com.example.sistemagestionbiblioteca.data.categories.Category
import com.example.sistemagestionbiblioteca.data.categories.CategoryResponse
import com.example.sistemagestionbiblioteca.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel encargado de gestionar las operaciones CRUD de categorías
 * y exponer el estado para la UI.
 *
 * @property categories LiveData con la lista de categorías actuales.
 * @property error LiveData con mensajes de error.
 * @property statusMessage LiveData con mensajes de estado o confirmación.
 */
class CategoryViewModel : ViewModel() {


    private val _categories = MutableLiveData<List<Category>>(emptyList())
    /**
     * LiveData público con la lista de categorías.
     */
    val categories: LiveData<List<Category>> = _categories

    private val _error = MutableLiveData<String?>()
    /**
     * LiveData público con mensajes de error.
     */
    val error: LiveData<String?> = _error

    // 1️⃣ Nuevo LiveData para mensajes de estado
    private val _statusMessage = MutableLiveData<String?>()
    /**
     * LiveData público con mensajes de estado o confirmación.
     */
    val statusMessage: LiveData<String?> = _statusMessage
    /**
     * Limpia el mensaje de estado para no mostrar nada.
     */
    fun clearStatusMessage() {
        _statusMessage.value = null
    }
    /**
     * Recupera todas las categorías desde la API y actualiza `categories`
     * o `error` según corresponda.
     */
    fun fetchCategories() {
        ApiService.getInstance().getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _categories.postValue(response.body())
                } else {
                    _error.postValue("Error ${response.code()} al cargar categorías")
                }
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                _error.postValue(t.message ?: "Error desconocido")
            }
        })
    }
    /**
     * Envía una petición para borrar una categoría y refresca la lista si tiene éxito.
     *
     * @param id Identificador de la categoría a eliminar.
     */
    fun deleteCategory(id: Int) {
        ApiService.getInstance().deleteCategory(id)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        fetchCategories()
                        _statusMessage.postValue(response.body() ?: "Categoría borrada")
                    } else {
                        _error.postValue("Error ${response.code()} al borrar categoría")
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _error.postValue(t.message ?: "Error desconocido")
                }
            })
    }
    /**
     * Envía una petición para actualizar una categoría y refresca la lista si tiene éxito.
     *
     * @param id Identificador de la categoría a actualizar.
     * @param nombre Nuevo nombre de la categoría.
     * @param descripcion Nueva descripción de la categoría (opcional).
     */
    fun updateCategory(id: Int, nombre: String, descripcion: String?) {
        val req = CategoryResponse(nombre, descripcion)
        ApiService.getInstance().updateCategory(id, req)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        fetchCategories()
                        _statusMessage.postValue("Categoría actualizada con éxito")
                    } else {
                        _error.postValue("Error ${response.code()} al actualizar categoría")
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _error.postValue(t.message ?: "Error desconocido")
                }
            })
    }
    /**
     * Envía una petición para crear una nueva categoría y refresca la lista si tiene éxito.
     *
     * @param nombre Nombre de la nueva categoría.
     * @param descripcion Descripción de la nueva categoría (opcional).
     */
    fun createCategory(nombre: String, descripcion: String?) {
        val req = CategoryResponse(nombre, descripcion)
        ApiService.getInstance().createCategory(req)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        fetchCategories()
                        _statusMessage.postValue("Categoría creada con éxito")
                    } else {
                        _error.postValue("Error ${response.code()} al crear categoría")
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _error.postValue(t.message ?: "Error desconocido")
                }
            })
    }
}

