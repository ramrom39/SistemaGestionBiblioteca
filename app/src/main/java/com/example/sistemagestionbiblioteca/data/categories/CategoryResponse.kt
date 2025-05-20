package com.example.sistemagestionbiblioteca.data.categories

/**
 * Representa los datos de una categoría de libros retornados por la API.
 *
 * @property nombre Nombre de la categoría.
 * @property descripcion Descripción opcional de la categoría.
 */

data class CategoryResponse(
    val nombre: String,
    val descripcion: String?
)