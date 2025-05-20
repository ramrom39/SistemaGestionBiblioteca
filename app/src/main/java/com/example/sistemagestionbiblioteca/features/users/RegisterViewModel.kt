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
/**
 * ViewModel encargado de gestionar el registro de nuevos usuarios.
 *
 * @property registerResponse LiveData con el mensaje de respuesta tras intentar registrar al usuario.
 * @property errorMessage LiveData con el mensaje de error en caso de fallo o conflicto (por ejemplo, usuario o correo duplicado).
 */
class RegisterViewModel : ViewModel() {

    private val _registerResponse = MutableLiveData<String>()

    /** LiveData público con el mensaje de éxito del registro. */
    val registerResponse: LiveData<String>
        get() = _registerResponse

    /** LiveData público con el mensaje de error del registro. */
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage
    /**
     * Envía los datos de registro a la API y gestiona la respuesta.
     *
     * @param user Objeto UserRegister con los datos del nuevo usuario.
     *                Puede generar mensajes de conflicto (código 409) si el usuario o correo ya existen.
     */
    fun registerUser(user: UserRegister) {
        ApiService.getInstance().registerUser(user).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _registerResponse.postValue(response.body()!!.message)
                } else if (response.code() == 409) {

                    val errorJson = response.errorBody()?.string()
                    val gson = Gson()
                    try {

                        if (errorJson?.contains("correo", ignoreCase = true) == true) {
                            _errorMessage.postValue("El correo electrónico ya existe")
                        } else {

                            val existResponse = gson.fromJson(errorJson, UserExistsResponse::class.java)
                            _errorMessage.postValue("El usuario ${existResponse.usuario} ya existe")
                        }
                    } catch (e: Exception) {
                        _errorMessage.postValue("Error 409: Registro duplicado")
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
    /**
     * Limpia el mensaje de error para permitir re-emisión futura.
     */
    fun clearError() {
        _errorMessage.value = ""
    }
}
