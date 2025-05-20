package com.example.sistemagestionbiblioteca.features.users


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemagestionbiblioteca.data.users.LoginResponse

import com.example.sistemagestionbiblioteca.data.users.UserLogin
import com.example.sistemagestionbiblioteca.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel encargado de gestionar el proceso de inicio de sesión de usuarios.
 *
 * @property loginResponse LiveData con el mensaje devuelto por la API tras intentar iniciar sesión.
 * @property errorMessage  LiveData con el mensaje de error en caso de fallo en la petición o credenciales incorrectas.
 * @property userId        LiveData con el ID del usuario autenticado, si el inicio de sesión es exitoso.
 */

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<String?>()
    val loginResponse: LiveData<String?> = _loginResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _userId = MutableLiveData<Int?>()
    val userId: LiveData<Int?> = _userId
    /**
     * Realiza la solicitud de inicio de sesión usando el nombre de usuario y la contraseña proporcionados.
     * Al limpiarse previamente los valores, garantiza que LiveData emita un nuevo estado.
     *
     * @param username Nombre de usuario para autenticar.
     * @param password Contraseña asociada al usuario.
     */
    fun loginUser(username: String, password: String) {

        _loginResponse.value = null
        _errorMessage.value  = null
        _userId.value        = null


        ApiService.getInstance()
            .loginUser(UserLogin(username, password))
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    c: Call<LoginResponse>,
                    resp: Response<LoginResponse>
                ) {
                    if (resp.isSuccessful && resp.body() != null) {
                        _loginResponse.value = resp.body()!!.message
                        _userId.value        = resp.body()!!.id
                    } else {
                        _errorMessage.value =
                            "Credenciales Incorrectas"
                    }
                }

                override fun onFailure(c: Call<LoginResponse>, t: Throwable) {
                    _errorMessage.value = "Error red: ${t.localizedMessage}"
                }
            })
    }
}
