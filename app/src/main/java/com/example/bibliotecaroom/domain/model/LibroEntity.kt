package com.example.bibliotecaroom.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//Data class de la clase Libro
@Entity(tableName = "libros")
data class LibroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val autor: String,
    val genero: String,
    val anioPublicacion: Int,
    val editorial: String,
    val paginas: Int,
    val disponible: Boolean
)