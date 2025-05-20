package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

/**
 * Representa un libro en la biblioteca.
 *
 * @property ID Identificador único del libro.
 * @property Título Título del libro.
 * @property Autor Autor del libro.
 * @property Año Año de publicación.
 * @property Sinopsis Resumen breve del contenido.
 * @property Categoría_ID Identificador de la categoría a la que pertenece.
 * @property Estado Estado actual del libro.
 * @property Fecha Fecha asociada al estado.
 * @property Estanteria_ID Identificador de la estantería donde se ubica el libro.
 */

data class Book(
    @SerializedName("ID")             val ID: Int,
    @SerializedName("Título")         val Título: String,
    @SerializedName("Autor")          val Autor: String,
    @SerializedName("Año")            val Año: Int,
    @SerializedName("Sinopsis")       val Sinopsis: String,
    @SerializedName("Categoría_ID")   val Categoría_ID: Int,
    @SerializedName("Estado")         val Estado: String,
    @SerializedName("Fecha")          val Fecha: String,
    @SerializedName("Estanteria_ID")  val Estanteria_ID: Int
)