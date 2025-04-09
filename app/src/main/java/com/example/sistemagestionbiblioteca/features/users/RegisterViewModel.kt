package com.example.sistemagestionbiblioteca.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemagestionbiblioteca.data.users.RegisterResponse
import com.example.sistemagestionbiblioteca.data.users.UserRegister
import com.example.sistemagestionbiblioteca.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    // LiveData para almacenar la respuesta del registro exitoso
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse>
        get() = _registerResponse

    // LiveData para errores en la llamada a la API
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    // Método que hace la llamada a la API para registrar el usuario.
    // Se denomina viewmodelUsers para mantener la nomenclatura que buscas.
    fun viewmodelUsers(user: UserRegister) {
        val call: Call<RegisterResponse> = ApiService.getInstance().registerUser(user)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    // Si la respuesta es exitosa, se actualiza el LiveData correspondiente
                    _registerResponse.value = response.body()
                } else {
                    // En caso de error, se asigna un mensaje de error con el código HTTP
                    _errorMessage.value = "Error en el registro: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Se actualiza el LiveData en caso de fallo en la conexión o error inesperado
                _errorMessage.value = t.message ?: "Error desconocido"
            }
        })
    }
}
