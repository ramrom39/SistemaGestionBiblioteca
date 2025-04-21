package com.example.sistemagestionbiblioteca.features.category

import androidx.lifecycle.*
import com.example.sistemagestionbiblioteca.data.categories.Category
import com.example.sistemagestionbiblioteca.data.categories.CategoryResponse
import com.example.sistemagestionbiblioteca.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>(emptyList())
    val categories: LiveData<List<Category>> = _categories

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

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

    fun deleteCategory(id: Int) {
        ApiService.getInstance().deleteCategory(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) fetchCategories()
                else _error.postValue("Error ${response.code()} al borrar categoría")
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                _error.postValue(t.message ?: "Error desconocido")
            }
        })
    }

    fun updateCategory(id: Int, nombre: String, descripcion: String?) {
        val req = CategoryResponse(nombre, descripcion)
        ApiService.getInstance().updateCategory(id, req).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) fetchCategories()
                else _error.postValue("Error ${response.code()} al actualizar categoría")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                _error.postValue(t.message ?: "Error desconocido")
            }
        })
    }


    fun createCategory(nombre: String, descripcion: String?) {
        val req = CategoryResponse(nombre, descripcion)
        ApiService.getInstance().createCategory(req).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    fetchCategories()
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
