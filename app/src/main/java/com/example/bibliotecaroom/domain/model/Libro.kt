package com.example.bibliotecaroom.domain.model

//Data class de la clase Libro
data class Libro(
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val genero: String,
    val anioPublicacion: Int,
    val editorial: String,
    val paginas: Int,
    val disponible: Boolean
)
