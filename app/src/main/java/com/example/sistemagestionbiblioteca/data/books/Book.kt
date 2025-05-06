package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

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