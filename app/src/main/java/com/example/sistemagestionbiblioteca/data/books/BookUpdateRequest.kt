package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

/**
 * Datos para actualizar un libro existente en el sistema.
 *
 * @property titulo Nuevo título del libro.
 * @property autor Nuevo autor del libro.
 * @property año Nuevo año de publicación.
 * @property sinopsis Nueva sinopsis o resumen.
 * @property categoria Nuevo identificador de categoría.
 * @property estado Nuevo estado del libro (por ejemplo, prestado, disponible).
 * @property fecha Nueva fecha asociada al estado o modificación.
 * @property estanteria Nuevo identificador de la estantería.
 * @property usuarioModificador Identificador del usuario que realiza la actualización.
 */

data class BookUpdateRequest(
    @SerializedName("titulo")               val titulo: String,
    @SerializedName("autor")                val autor: String,
    @SerializedName("año")                  val año: Int,
    @SerializedName("sinopsis")             val sinopsis: String,
    @SerializedName("categoria")            val categoria: Int,
    @SerializedName("estado")               val estado: String,
    @SerializedName("fecha")                val fecha: String,
    @SerializedName("estanteria")           val estanteria: Int,
    @SerializedName("usuario_modificador")  val usuarioModificador: Int
)