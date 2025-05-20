package com.example.sistemagestionbiblioteca.data.users

/**
 * Datos de entrada para autenticar a un usuario en el sistema.
 *
 * @property username Nombre de usuario (username) utilizado para el inicio de sesión.
 * @property password Contraseña asociada al usuario.
 */
data class UserLogin(
    val username: String,
    val password: String)