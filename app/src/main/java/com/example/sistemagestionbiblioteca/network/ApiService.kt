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
import okhttp3.OkHttpClient
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
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
/**
 * Servicio API para la comunicación con el backend de la biblioteca.
 *
 * Provee métodos para gestionar usuarios, categorías, libros, historial y estanterías.
 *
 * Usa Retrofit con un cliente OkHttp personalizado que incluye:
 * - TrustManager que no valida certificados (solo para desarrollo).
 * - Interceptor para agregar la API_KEY en el header Authorization.
 */
interface ApiService {

    companion object {
        // Cambia localhost por la IP del host si usas un emulador (ej. 10.0.2.2)
        private const val BASE_URL =
            "https://ramonromerodev.alumnosatlantida.es/APIBIBLIOTECA/controller/"
        private const val API_KEY = "eb904f62-1445-4eb6-a9ff-9e497af1a512"
        private var apiService: ApiService? = null
        /**
         * Devuelve la instancia singleton de ApiService.
         *
         * Inicializa Retrofit con:
         * - URL base: BASE_URL
         * - OkHttpClient inseguro que omite validación SSL.
         * - Interceptor de autenticación con API_KEY.
         */
        fun getInstance(): ApiService {
            if (apiService == null) {

                val trustAll = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                    }
                )
                val sc = SSLContext.getInstance("SSL").apply {
                    init(null, trustAll, SecureRandom())
                }
                val socketFactory = sc.socketFactory

                val client = OkHttpClient.Builder()
                    .sslSocketFactory(socketFactory, trustAll[0] as X509TrustManager)
                    .hostnameVerifier { _, _ -> true }
                    .addInterceptor { chain ->
                        val newReq = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $API_KEY")
                            .build()
                        chain.proceed(newReq)
                    }
                    .build()

                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }
    /**
     * Obtiene los datos de un usuario por su ID.
     *
     * @param id ID del usuario a consultar.
     * @return UserNameResponse con el ID y nombre.
     */
    @GET("usuariosController.php")
    suspend fun getUserById(@Query("id") id: Int): UserNameResponse

    /**
     * Registra un nuevo usuario.
     *
     * @param user Datos del usuario a registrar.
     * @return Call con RegisterResponse indicando éxito o error.
     */
    @Headers("Content-Type: application/json")
    @POST("registroController.php")
    fun registerUser(@Body user: UserRegister): Call<RegisterResponse>

    /**
     * Inicia sesión de un usuario.
     *
     * @param request Credenciales para autenticación.
     * @return Call con LoginResponse con mensaje e ID.
     */
    @POST("loginController.php")
    fun loginUser(@Body request: UserLogin): Call<LoginResponse>

    /**
     * Recupera todas las categorías.
     *
     * @return Call con la lista de Category.
     */
    @GET("categoriasController.php")
    fun getCategories(): Call<List<Category>>

    /**
     * Elimina una categoría por ID.
     *
     * @param id ID de la categoría a eliminar.
     * @return Call con mensaje de resultado.
     */
    @DELETE("categoriasController.php")
    fun deleteCategory(@Query("id") id: Int): Call<String>

    /**
     * Actualiza una categoría existente.
     *
     * @param id ID de la categoría a actualizar.
     * @param request Datos nuevos de la categoría.
     * @return Call con mensaje de resultado.
     */
    @PUT("categoriasController.php")
    fun updateCategory(
        @Query("id") id: Int,
        @Body request: CategoryResponse
    ): Call<String>

    /**
     * Crea una nueva categoría.
     *
     * @param request Datos de la nueva categoría.
     * @return Call con mensaje de resultado.
     */
    @Headers("Content-Type: application/json")
    @POST("categoriasController.php")
    fun createCategory(@Body request: CategoryResponse): Call<String>

    /**
     * Obtiene todos los libros.
     *
     * @return Call con la lista de Book.
     */
    @GET("librosController.php")
    fun getBooks(): Call<List<Book>>

    /**
     * Crea un nuevo libro.
     *
     * @param body Datos para la creación del libro.
     * @return Call con BookResponse con mensaje e ID.
     */
    @Headers("Content-Type: application/json")
    @POST("librosController.php")
    fun createBook(@Body body: BookCreateRequest): Call<BookResponse>
    /**
     * Actualiza un libro existente.
     *
     * @param id ID del libro a actualizar.
     * @param request Nuevos datos del libro.
     * @return Call con BookResponse con mensaje.
     */
    @PUT("librosController.php")
    fun updateBook(
        @Query("id") id: Int,
        @Body request: BookUpdateRequest
    ): Call<BookResponse>

    /**
     * Elimina un libro por su ID.
     *
     * @param id ID del libro a borrar.
     * @return Call con BookResponse con mensaje.
     */
    @DELETE("librosController.php")
    fun deleteBook(@Query("id") id: Int): Call<BookResponse>

    /**
     * Recupera el historial de un libro por su ID.
     *
     * @param bookId ID del libro.
     * @return Lista de History con todas las acciones.
     */
    @GET("historialController.php")
    suspend fun getHistoryByBookId(@Query("libroId") bookId: Int): List<History>

    /**
     * Obtiene todas las estanterías.
     *
     * @return Response con lista de Shelf.
     */
    @GET("estanteriasController.php")
    suspend fun getShelves(): Response<List<Shelf>>

    /**
     * Obtiene los libros de una estantería específica.
     *
     * @param shelfId ID de la estantería.
     * @return Response con lista de Book.
     */
    @GET("estanteriasController.php")
    suspend fun getBooksByShelf(
        @Query("shelfId") shelfId: Int
    ): Response<List<Book>>

}

