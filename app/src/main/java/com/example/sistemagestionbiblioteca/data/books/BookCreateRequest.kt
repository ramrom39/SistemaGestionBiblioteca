package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

/**
 * Datos para la creación de un nuevo libro en el sistema.
 *
 * @property titulo Título del libro.
 * @property autor Autor del libro.
 * @property año Año de publicación.
 * @property sinopsis Resumen breve del contenido.
 * @property categoria Identificador de la categoría del libro.
 * @property estado Estado inicial del libro.
 * @property fecha Fecha asociada al registro.
 * @property estanteria Identificador de la estantería donde se ubicará.
 */

data class BookCreateRequest(

    @SerializedName("titulo")    val titulo: String,

    @SerializedName("autor")     val autor: String,

    @SerializedName("año")       val año: Int,

    @SerializedName("sinopsis")  val sinopsis: String,

    @SerializedName("categoria") val categoria: Int,

    @SerializedName("estado")    val estado: String,

    @SerializedName("fecha")     val fecha: String,

    @SerializedName("estanteria")val estanteria: Int

)
