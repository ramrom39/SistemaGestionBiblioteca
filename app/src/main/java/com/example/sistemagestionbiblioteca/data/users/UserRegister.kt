package com.example.sistemagestionbiblioteca.data.users

/**
 * Datos necesarios para registrar un nuevo usuario en el sistema.
 *
 * @property nombre Nombre de pila del usuario.
 * @property apellidos Apellidos del usuario.
 * @property usuario Nombre de usuario (username) elegido.
 * @property contraseña Contraseña del usuario (debe coincidir con el campo esperado por PHP).
 * @property correoElectronico Correo electrónico del usuario.
 */

data class UserRegister(
    val nombre: String,
    val apellidos: String,
    val usuario: String,
    val contraseña: String,
    val correoElectronico: String
)
