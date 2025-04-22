package com.example.sistemagestionbiblioteca.data.books

import com.google.gson.annotations.SerializedName

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
