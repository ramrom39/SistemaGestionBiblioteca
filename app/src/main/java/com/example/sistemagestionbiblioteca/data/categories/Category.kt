package com.example.sistemagestionbiblioteca.data.categories

import com.google.gson.annotations.SerializedName

/**
 * Representa una categoría de libros en la biblioteca.
 *
 * @property id Identificador único de la categoría.
 * @property nombre Nombre de la categoría.
 * @property descripcion Descripción opcional de la categoría.
 */

data class Category(
    @SerializedName("ID")          val id: Int,
    @SerializedName("Nombre")      val nombre: String,
    @SerializedName("Descripcion") val descripcion: String?
)
