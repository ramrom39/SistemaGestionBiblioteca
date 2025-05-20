package com.example.sistemagestionbiblioteca.data.history

import com.google.gson.annotations.SerializedName

/**
 * Representa un registro histórico de acciones realizadas sobre un libro en la biblioteca.
 *
 * @property id Identificador único del registro de historial.
 * @property bookId Identificador del libro al que se aplica la acción.
 * @property date Fecha en que se realizó la acción.
 * @property actionType Tipo de acción realizada (por ejemplo, préstamo o devolución).
 * @property userId Identificador del usuario que efectuó la acción.
 */

data class History(
    @SerializedName("ID")                 val id: Int,
    @SerializedName("ID_Libro")           val bookId: Int,
    @SerializedName("Fecha")              val date: String,
    @SerializedName("Tipo_accion")        val actionType: String,
    @SerializedName("Usuario_modificador")val userId: Int
)
