package com.example.sistemagestionbiblioteca.data.shelves

import com.google.gson.annotations.SerializedName

/**
 * Representa una estantería en la biblioteca.
 *
 * @property id Identificador único de la estantería.
 * @property ubicacion Ubicación o nombre de la estantería.
 * @property descripcion Descripción de la estantería.
 */

data class Shelf(
    @SerializedName("ID")           val id: Int,
    @SerializedName("Ubicacion")    val ubicacion: String,
    @SerializedName("Descripcion")  val descripcion: String
)