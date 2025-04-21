package com.example.sistemagestionbiblioteca.network

import com.example.sistemagestionbiblioteca.data.categories.Category

import com.example.sistemagestionbiblioteca.data.categories.CategoryResponse
import com.example.sistemagestionbiblioteca.data.users.LoginResponse
import com.example.sistemagestionbiblioteca.data.users.RegisterResponse

import com.example.sistemagestionbiblioteca.data.users.UserRegister
import com.example.sistemagestionbiblioteca.data.users.UserLogin
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    companion object {
        // Cambia localhost por la IP del host si usas un emulador (ej. 10.0.2.2)
        private const val BASE_URL =
            "https://ramonromerodev.alumnosatlantida.es/APIBIBLIOTECA/controller/"
        private var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }

    @Headers("Content-Type: application/json")
    // Usamos el objeto User para enviar la solicitud y esperamos un RegisterResponse como respuesta.
    @POST("registroController.php")
    fun registerUser(@Body user: UserRegister): Call<RegisterResponse>

    @POST("loginController.php")
    fun loginUser(@Body request: UserLogin): Call<LoginResponse>

    @GET("categoriasController.php")
    fun getCategories(): Call<List<Category>>

    @HTTP(method = "DELETE", path = "categoriasController.php", hasBody = false)
    fun deleteCategory(@Query("id") id: Int): Call<Void>

    @PUT("categoriasController.php")
    fun updateCategory(
        @Query("id") id: Int,
        @Body request: CategoryResponse
    ): Call<String>

    @Headers("Content-Type: application/json")
    @POST("categoriasController.php")
    fun createCategory(@Body request: CategoryResponse): Call<String>
}

