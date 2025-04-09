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

class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<String>()
    val loginResponse: LiveData<String>
        get() = _loginResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun loginUser(username: String, password: String) {
        // Utilizamos la data class UserLogin con los campos correctos
        val userLogin = UserLogin(username = username, password = password)
        ApiService.getInstance().loginUser(userLogin).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _loginResponse.postValue(response.body()!!.message)
                } else {
                    _errorMessage.postValue("Error ${response.code()}: No se pudo iniciar sesión")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _errorMessage.postValue(t.localizedMessage ?: "Error desconocido")
            }
        })
    }
}
