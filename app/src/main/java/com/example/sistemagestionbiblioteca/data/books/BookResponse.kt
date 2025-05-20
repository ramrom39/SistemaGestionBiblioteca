package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de la API tras una operación con libros.
 *
 * @property message Mensaje que indica el resultado de la operación.
 * @property id Identificador del libro afectado (por ejemplo, creado o actualizado).
 */

data class BookResponse(
    @SerializedName("message") val message: String,
    @SerializedName("id")      val id: Int
)