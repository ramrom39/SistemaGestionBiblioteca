package com.example.sistemagestionbiblioteca.data.books

data class Book(
    val ID: Int,
    val Título: String,
    val Autor: String,
    val Año: Int,
    val Sinopsis: String,
    val Categoría_ID: Int,
    val Estado: String,
    val Fecha: String,
    val Estanteria_ID: Int
)