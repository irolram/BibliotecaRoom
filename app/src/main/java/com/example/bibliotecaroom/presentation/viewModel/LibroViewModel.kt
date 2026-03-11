package com.example.bibliotecaroom.presentation.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bibliotecaroom.data.repository.IBibliotecaRepository
import com.example.bibliotecaroom.domain.model.Libro
import com.example.bibliotecaroom.domain.usecase.InsertarLibroUseCase
import com.example.bibliotecaroom.domain.usecase.PrestarLibroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibroViewModel @Inject constructor(
    private val repository: IBibliotecaRepository,
    private val insertarLibroUseCase: InsertarLibroUseCase,
    private val prestarLibroUseCase: PrestarLibroUseCase
) : ViewModel() {

    // Estado de la lista de libros y del libro seleccionado
    val librosFlow: Flow<List<Libro>> = repository.obtenerTodosLosLibros()
    var libroSeleccionado by mutableStateOf<Libro?>(null)

    // Estado para mostrar errores en la UI (Toasts o mensajes)
    var mensajeError by mutableStateOf<String?>(null)

    /**
     * UNIFICADO: Esta es la función que debes llamar desde AltaLibroScreen.
     * Ahora incluye un callback 'onExito' para que la pantalla solo se cierre si es válido.
     */
    fun guardarNuevoLibro(
        titulo: String,
        autor: String,
        anio: Int,
        genero: String,
        editorial: String,
        paginas: Int,
        disponible: Boolean,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            val nuevoLibro = Libro(
                titulo = titulo,
                autor = autor,
                anioPublicacion = anio,
                genero = genero,
                editorial = editorial,
                paginas = paginas,
                disponible = disponible
            )

            // USAMOS EL USE CASE (Aquí es donde el -5 fallará)
            val resultado = insertarLibroUseCase(nuevoLibro)

            resultado.onSuccess {
                mensajeError = null
                onExito() // Solo volvemos atrás si el Use Case da el OK
            }.onFailure { error ->
                // Guardamos el error para que la UI pueda mostrarlo
                mensajeError = error.message
            }
        }
    }

    fun actualizarLibro(libro: Libro, onExito: () -> Unit) {
        viewModelScope.launch {
            // También deberías usar el UseCase aquí si quieres validar al editar
            repository.actualizarLibro(libro)
            onExito()
        }
    }

    fun eliminarLibro(libro: Libro) {
        viewModelScope.launch {
            repository.eliminarLibro(libro)
        }
    }

    fun prestarLibro(libro: Libro) {
        viewModelScope.launch {
            val resultado = prestarLibroUseCase(libro)
            resultado.onSuccess {
                mensajeError = null
            }.onFailure { error ->
                mensajeError = error.message
            }
        }
    }

    fun obtenerLibroPorId(id: Int): Flow<Libro?> {
        return repository.obtenerLibroPorId(id)
    }
}