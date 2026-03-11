package com.example.bibliotecaroom.domain.usecase

import com.example.bibliotecaroom.data.repository.IBibliotecaRepository
import com.example.bibliotecaroom.domain.model.Libro

class InsertarLibroUseCase(private val repo: IBibliotecaRepository) {

    suspend operator fun invoke(libro: Libro): Result<Unit> {
        // 1. Validación de campos vacíos
        // Usamos 'if' para devolver el fallo sin lanzar una excepción que rompa la app
        if (libro.titulo.isBlank() || libro.autor.isBlank()) {
            return Result.failure(Exception("El título y el autor son obligatorios"))
        }

        // 2. Validación de lógica numérica (Año y Páginas)
        // El rango 1..2026 es más estándar y seguro en Android
        if (libro.anioPublicacion !in 1..2026) {
            return Result.failure(Exception("El año de publicación debe estar entre 1 y 2026"))
        }

        if (libro.paginas <= 0) {
            return Result.failure(Exception("El número de páginas debe ser mayor a 0"))
        }

        // 3. Validación de longitud
        if (libro.genero.length < 3) {
            return Result.failure(Exception("El género debe tener al menos 3 caracteres"))
        }

        // 4. Si todas las validaciones pasan, procedemos a la base de datos
        return try {
            repo.insertarLibro(libro)
            Result.success(Unit)
        } catch (e: Exception) {
            // Capturamos errores inesperados de Room (ej. disco lleno, conflicto de ID)
            Result.failure(e)
        }
    }
}