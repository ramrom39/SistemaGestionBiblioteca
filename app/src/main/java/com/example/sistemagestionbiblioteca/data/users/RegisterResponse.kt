package com.example.sistemagestionbiblioteca.data.users

/**
 * Representa la respuesta de la API tras un registro de usuario.
 *
 * @property message Mensaje que indica el resultado del registro (por ejemplo, éxito o error).
 * @property id Identificador del nuevo usuario creado.
 */

data class RegisterResponse(
    val message: String,
    val id: Int
)
