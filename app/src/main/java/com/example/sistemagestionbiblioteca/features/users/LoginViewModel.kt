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
    val loginResponse: LiveData<String> = _loginResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> = _userId

    fun loginUser(username: String, password: String) {
        ApiService.getInstance().loginUser(UserLogin(username, password))
            .enqueue(object: Callback<LoginResponse> {
                override fun onResponse(c: Call<LoginResponse>, resp: Response<LoginResponse>) {
                    if (resp.isSuccessful && resp.body()!=null) {
                        _loginResponse.value = resp.body()!!.message
                        _userId.value       = resp.body()!!.id   // <-- guardamos aquí el id
                    } else {
                        _errorMessage.value = "Error ${resp.code()}: ${resp.errorBody()?.string()}"
                    }
                }
                override fun onFailure(c: Call<LoginResponse>, t: Throwable) {
                    _errorMessage.value = "Error red: ${t.localizedMessage}"
                }
            })
    }
}
