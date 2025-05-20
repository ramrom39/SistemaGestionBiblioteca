package com.example.sistemagestionbiblioteca.data.users

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de la API donde se solicita el nombre de un usuario por su ID.
 *
 * @property id Identificador único del usuario.
 * @property nombre Nombre completo del usuario.
 */
data class UserNameResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String)
