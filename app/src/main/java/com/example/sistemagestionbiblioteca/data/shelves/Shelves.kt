package com.example.sistemagestionbiblioteca.data.shelves

import com.google.gson.annotations.SerializedName

data class Shelf(
    @SerializedName("ID")           val id: Int,
    @SerializedName("Ubicacion")    val ubicacion: String,
    @SerializedName("Descripcion")  val descripcion: String
)