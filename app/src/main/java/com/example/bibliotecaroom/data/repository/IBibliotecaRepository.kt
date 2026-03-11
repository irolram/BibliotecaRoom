package com.example.bibliotecaroom.data.repository

import com.example.bibliotecaroom.domain.model.Libro
import kotlinx.coroutines.flow.Flow

interface IBibliotecaRepository {

    fun obtenerLibroPorId(id: Int): Flow<Libro?>
    suspend fun insertarLibro(libro: Libro)
    suspend fun eliminarLibro(libro: Libro)
    suspend fun actualizarLibro(libro: Libro)
    suspend fun actualizarEstadoPrestamo(id: Int, estaPrestado: Boolean): Boolean
    fun obtenerTodosLosLibros(): Flow<List<Libro>>
}