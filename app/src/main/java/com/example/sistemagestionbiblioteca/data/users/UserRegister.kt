package com.example.sistemagestionbiblioteca.data.users

data class UserRegister(
    val nombre: String,
    val apellidos: String,
    val usuario: String,
    // Es importante que el nombre del campo coincida con lo que espera PHP.
    // Tu RegistroController.php usa $data['contraseña'], así que puede ser:
    val contraseña: String
)
