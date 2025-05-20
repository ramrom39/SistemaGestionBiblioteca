package com.example.sistemagestionbiblioteca.data.users

/**
 * Representa la respuesta de la API al verificar si un usuario existe.
 *
 * @property message Mensaje que indica el resultado de la verificación (por ejemplo, éxito o error).
 * @property nombre Nombre completo del usuario registrado (si existe).
 * @property usuario Nombre de usuario (username) verificado.
 */

data class UserExistsResponse(
    val message: String,
    val nombre: String,
    val usuario: String
)