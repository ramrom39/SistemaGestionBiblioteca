package com.example.sistemagestionbiblioteca.data.history

import com.google.gson.annotations.SerializedName

// data/history/History.kt
data class History(
    @SerializedName("ID")                 val id: Int,
    @SerializedName("ID_Libro")           val bookId: Int,
    @SerializedName("Fecha")              val date: String,
    @SerializedName("Tipo_accion")        val actionType: String,
    @SerializedName("Usuario_modificador")val userId: Int
)
