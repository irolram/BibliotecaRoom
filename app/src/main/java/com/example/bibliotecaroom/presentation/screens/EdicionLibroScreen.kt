package com.example.bibliotecaroom.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel
import com.example.bibliotecaroom.presentation.components.FormularioLibro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdicionLibroScreen(
    onVolver: () -> Unit,
    viewModel: LibroViewModel
) {
    val libro = viewModel.libroSeleccionado

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Libro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (libro != null) {
                FormularioLibro(
                    tituloInicial = libro.titulo,
                    autorInicial = libro.autor,
                    anioInicial = libro.anioPublicacion.toString(),
                    generoInicial = libro.genero,
                    editorialInicial = libro.editorial,
                    paginasInicial = libro.paginas.toString(),
                    disponibleInicial = libro.disponible,
                    textoBoton = "Actualizar Cambios",
                    onGuardarClick = { t, a, anStr, g, e, pStr, d ->

                        // 1. Convertimos a Int para que el objeto Libro lo acepte
                        val anioInt = anStr.toString().toIntOrNull() ?: 0
                        val paginasInt = pStr.toString().toIntOrNull() ?: 0

                        // 2. Creamos la copia con los tipos correctos
                        val editado = libro.copy(
                            titulo = t,
                            autor = a,
                            anioPublicacion = anioInt, // Ahora es Int
                            genero = g,
                            editorial = e,
                            paginas = paginasInt,      // Ahora es Int
                            disponible = d
                        )

                        // 3. Usamos el callback onExito para que solo vuelva si es válido
                        viewModel.actualizarLibro(
                            libro = editado,
                            onExito = { onVolver() }
                        )
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se ha seleccionado ningún libro para editar.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}