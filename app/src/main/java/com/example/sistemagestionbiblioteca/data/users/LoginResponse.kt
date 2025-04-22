package com.example.sistemagestionbiblioteca.data.users

data class LoginResponse(
    val message: String,
    val id: Int       // ahora mapea el campo "id" que devuelve PHP
)