package com.example.sistemagestionbiblioteca.data.categories

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("ID")          val id: Int,
    @SerializedName("Nombre")      val nombre: String,
    @SerializedName("Descripcion") val descripcion: String?
)
