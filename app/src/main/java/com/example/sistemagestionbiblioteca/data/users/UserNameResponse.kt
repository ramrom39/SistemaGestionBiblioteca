package com.example.sistemagestionbiblioteca.data.users

import com.google.gson.annotations.SerializedName

data class UserNameResponse(@SerializedName("id")     val id: Int,
                            @SerializedName("nombre") val nombre: String)
