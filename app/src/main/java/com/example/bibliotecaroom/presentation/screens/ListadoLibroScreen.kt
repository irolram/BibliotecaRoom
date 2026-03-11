package com.example.bibliotecaroom.presentation.screens.listado

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bibliotecaroom.domain.model.Libro
import com.example.bibliotecaroom.presentation.components.ItemLibro
import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoLibrosScreen(
    onNavigateToAlta: () -> Unit,
    onNavigateToDetalle: (Int) -> Unit,
    onNavigateToEdicion: (Int) -> Unit,
    viewModel: LibroViewModel = hiltViewModel()
) {

    val libros by viewModel.librosFlow.collectAsState(initial = emptyList())


    // 2. Estados locales para el diálogo de confirmación de borrado
    var mostrarDialogo by remember { mutableStateOf(false) }
    var libroSeleccionado by remember { mutableStateOf<Libro?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Biblioteca") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAlta,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Libro")
            }
        }
    ) { paddingValues ->

        // 3. Lógica de visualización
        if (libros.isEmpty()) {
            // Pantalla vacía si no hay datos
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu biblioteca está vacía.\n¡Empieza añadiendo tu primer libro!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(
                    items = libros,
                    key = { it.id }
                ) { libro ->
                    ItemLibro(
                        libro = libro,
                        onVerDetalle = { onNavigateToDetalle(libro.id) },
                        onEditar = { onNavigateToEdicion(libro.id) },
                        onBorrar = {
                            libroSeleccionado = libro
                            mostrarDialogo = true
                        }
                    )
                }
            }
        }
    }

    // 4. Diálogo de borrado (Solo se muestra si mostrarDialogo es true)
    if (mostrarDialogo && libroSeleccionado != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
                libroSeleccionado = null
            },
            title = { Text("¿Eliminar este libro?") },
            text = { Text("¿Seguro que quieres borrar '${libroSeleccionado?.titulo}'? Esta acción es permanente.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        libroSeleccionado?.let { viewModel.eliminarLibro(it) }
                        mostrarDialogo = false
                        libroSeleccionado = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    libroSeleccionado = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}