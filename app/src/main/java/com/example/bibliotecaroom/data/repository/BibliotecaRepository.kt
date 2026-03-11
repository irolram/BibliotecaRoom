package com.example.bibliotecaroom.data.repository

import com.example.bibliotecaroom.domain.model.Libro
import com.example.bibliotecaroom.domain.model.LibroEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BibliotecaRepositoryImpl @Inject constructor(
    private val libroDao: LibroDao
) : IBibliotecaRepository {

    override fun obtenerTodosLosLibros(): Flow<List<Libro>> {
        return libroDao.obtenerTodos().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override fun obtenerLibroPorId(id: Int): Flow<Libro?> {
        return libroDao.obtenerPorId(id).map { it?.toDomain() }
    }

    override suspend fun insertarLibro(libro: Libro) {
        libroDao.insertar(libro.toEntity())
    }

    override suspend fun eliminarLibro(libro: Libro) {
        libroDao.eliminar(libro.toEntity())
    }

    override suspend fun actualizarLibro(libro: Libro) {
        libroDao.actualizar(libro.toEntity())
    }

    override suspend fun actualizarEstadoPrestamo(id: Int, estaPrestado: Boolean): Boolean {
        libroDao.actualizarEstadoPrestamo(id, estaPrestado)
        return true
    }
}

fun LibroEntity.toDomain() = Libro(id, titulo, autor, genero, anioPublicacion, editorial, paginas, disponible)
fun Libro.toEntity() = LibroEntity(id, titulo, autor, genero, anioPublicacion, editorial, paginas, disponible)