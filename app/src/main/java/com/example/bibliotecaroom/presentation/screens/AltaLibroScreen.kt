package com.example.bibliotecaroom.presentation.screens

import android.view.Surface
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bibliotecaroom.presentation.components.FormularioLibro
import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel

// Composable para la pantalla de alta de libro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltaLibroScreen(
    onVolver: () -> Unit,
    viewModel: LibroViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dar de Alta Libro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            FormularioLibro(
                textoBoton = "Guardar en Biblioteca",
                onGuardarClick = { titulo, autor, anioStr, genero, editorial, paginasStr, disponible ->
                    val anioInt = anioStr.toString().toIntOrNull() ?: 0
                    val paginasInt = paginasStr.toString().toIntOrNull() ?: 0

                    viewModel.guardarNuevoLibro(
                        titulo = titulo,
                        autor = autor,
                        anio = anioInt,
                        genero = genero,
                        editorial = editorial,
                        paginas = paginasInt,
                        disponible = disponible,
                        onExito = { onVolver() }
                    )
                }
            )
        }
    }
}