package com.example.bibliotecaroom.domain.usecase

import com.example.bibliotecaroom.data.repository.IBibliotecaRepository
import com.example.bibliotecaroom.domain.model.Libro

class PrestarLibroUseCase(private val repo: IBibliotecaRepository) {

    suspend operator fun invoke(libro: Libro): Result<Unit> {
        // 1. Validación: Si el libro ya no está disponible, no se puede prestar
        if(libro.disponible) {
            return Result.failure(Exception("El libro ya se encuentra prestado"))
        }

        // 2. Lógica: Creamos una copia del libro cambiando el estado de disponibilidad
        val libroPrestado = libro.copy(disponible = false)

        return try {
            // 3. Acción: Actualizamos el libro en la base de datos
            repo.actualizarLibro(libroPrestado)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}