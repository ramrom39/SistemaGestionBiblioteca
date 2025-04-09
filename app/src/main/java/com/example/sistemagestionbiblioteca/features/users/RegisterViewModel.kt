package com.example.sistemagestionbiblioteca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemagestionbiblioteca.data.users.RegisterResponse
import com.example.sistemagestionbiblioteca.data.users.UserExistsResponse
import com.example.sistemagestionbiblioteca.data.users.UserRegister
import com.example.sistemagestionbiblioteca.network.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<String>()
    val registerResponse: LiveData<String>
        get() = _registerResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun registerUser(user: UserRegister) {
        ApiService.getInstance().registerUser(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _registerResponse.postValue(response.body()!!.message)
                } else if (response.code() == 409) {
                    // Si recibimos un conflicto, parseamos el error para obtener los datos del usuario existente.
                    val errorJson = response.errorBody()?.string()
                    val gson = Gson()
                    try {
                        val existResponse = gson.fromJson(errorJson, UserExistsResponse::class.java)
                        _errorMessage.postValue("El usuario ${existResponse.usuario} ya existe")
                    } catch (e: Exception) {
                        _errorMessage.postValue("El usuario ya existe")
                    }
                } else {
                    _errorMessage.postValue("Error ${response.code()}: No se pudo registrar el usuario")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _errorMessage.postValue(t.localizedMessage ?: "Error desconocido")
            }
        })
    }
}
