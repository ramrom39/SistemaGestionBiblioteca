package com.example.sistemagestionbiblioteca.network

import com.example.sistemagestionbiblioteca.data.books.Book
import com.example.sistemagestionbiblioteca.data.books.BookCreateRequest
import com.example.sistemagestionbiblioteca.data.books.BookResponse
import com.example.sistemagestionbiblioteca.data.books.BookUpdateRequest
import com.example.sistemagestionbiblioteca.data.categories.Category

import com.example.sistemagestionbiblioteca.data.categories.CategoryResponse
import com.example.sistemagestionbiblioteca.data.history.History
import com.example.sistemagestionbiblioteca.data.shelves.Shelf
import com.example.sistemagestionbiblioteca.data.users.LoginResponse
import com.example.sistemagestionbiblioteca.data.users.RegisterResponse
import com.example.sistemagestionbiblioteca.data.users.User

import com.example.sistemagestionbiblioteca.data.users.UserRegister
import com.example.sistemagestionbiblioteca.data.users.UserLogin
import com.example.sistemagestionbiblioteca.data.users.UserNameResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @GET("usuariosController.php")
    suspend fun getUserById(@Query("id") id: Int): UserNameResponse

    @Headers("Content-Type: application/json")
    @POST("registroController.php")
    fun registerUser(@Body user: UserRegister): Call<RegisterResponse>

    @POST("loginController.php")
    fun loginUser(@Body request: UserLogin): Call<LoginResponse>

    @GET("categoriasController.php")
    fun getCategories(): Call<List<Category>>

    @DELETE("categoriasController.php")
    fun deleteCategory(@Query("id") id: Int): Call<String>

    @PUT("categoriasController.php")
    fun updateCategory(
        @Query("id") id: Int,
        @Body request: CategoryResponse
    ): Call<String>

    @Headers("Content-Type: application/json")
    @POST("categoriasController.php")
    fun createCategory(@Body request: CategoryResponse): Call<String>

    @GET("librosController.php")
    fun getBooks(): Call<List<Book>>

    @Headers("Content-Type: application/json")
    @POST("librosController.php")
    fun createBook(@Body body: BookCreateRequest): Call<BookResponse>

    @PUT("librosController.php")
    fun updateBook(
        @Query("id") id: Int,
        @Body request: BookUpdateRequest
    ): Call<BookResponse>

    @DELETE("librosController.php")
    fun deleteBook(@Query("id") id: Int): Call<BookResponse>

    @GET("historialController.php")
    suspend fun getHistoryByBookId(@Query("libroId") bookId: Int): List<History>

    // Rutas de estanterías y libros
    @GET("estanteriasController.php")
    suspend fun getShelves(): Response<List<Shelf>>


    @GET("estanteriasController.php")
    suspend fun getBooksByShelf(
        @Query("shelfId") shelfId: Int
    ): Response<List<Book>>

}

