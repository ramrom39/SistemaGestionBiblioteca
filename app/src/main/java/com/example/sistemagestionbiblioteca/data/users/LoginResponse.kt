package com.example.sistemagestionbiblioteca.data.users

/**
 * Representa la respuesta de la API tras un intento de inicio de sesión.
 *
 * @property message Mensaje que indica el resultado del inicio de sesión (por ejemplo, éxito o error).
 * @property id Identificador del usuario autenticado, según lo devuelto por el servidor.
 */

data class LoginResponse(
    val message: String,
    val id: Int       // ahora mapea el campo "id" que devuelve PHP
)