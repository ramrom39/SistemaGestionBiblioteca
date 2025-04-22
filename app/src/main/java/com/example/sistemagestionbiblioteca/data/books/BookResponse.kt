package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("message") val message: String,
    @SerializedName("id")      val id: Int
)