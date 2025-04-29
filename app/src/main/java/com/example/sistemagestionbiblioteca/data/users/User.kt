package com.example.sistemagestionbiblioteca.data.users

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ID")     val id: Int,
    @SerializedName("Nombre") val nombre: String
)