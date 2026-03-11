package com.example.bibliotecaroom.presentation.screens.listado

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bibliotecaroom.domain.model.Libro
import com.example.bibliotecaroom.domain.model.Pantalla
import com.example.bibliotecaroom.presentation.components.ItemLibro
import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObtenerTodosLosLibrosScreen(
    viewModel: LibroViewModel,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val libros by viewModel.librosFlow.collectAsState(initial = emptyList())

    var textoBusqueda by remember { mutableStateOf("") }


    val librosFiltrados = remember(textoBusqueda, libros) {
        if (textoBusqueda.isEmpty()) {
            libros
        } else {
            val idBuscado = textoBusqueda.toIntOrNull()
            if (idBuscado != null) {
                libros.filter { it.id == idBuscado }
            } else {
                libros
            }
        }
    }

    var mostrarDialogoBorrado by remember { mutableStateOf(false) }
    var libroABorrar by remember { mutableStateOf<Libro?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Listado de Libros") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {


            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Buscar por ID de libro") },
                placeholder = { Text("Ej: 5") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = librosFiltrados, key = { it.id }) { libro ->
                    ItemLibro(
                        libro = libro,
                        onVerDetalle = {
                            viewModel.libroSeleccionado = libro
                            onNavigate(Pantalla.GetById.route)
                        },
                        onEditar = {
                            viewModel.libroSeleccionado = libro
                            onNavigate(Pantalla.Update.route)
                        },
                        onBorrar = {
                            libroABorrar = libro
                            mostrarDialogoBorrado = true
                        }
                    )
                }

                if (librosFiltrados.isEmpty() && textoBusqueda.isNotEmpty()) {
                    item {
                        Text(
                            "No se encontró ningún libro con el ID $textoBusqueda",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        // Diálogo de confirmación
        if (mostrarDialogoBorrado) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrado = false },
                title = { Text("¿Eliminar libro?") },
                text = { Text("¿Estás seguro de eliminar '${libroABorrar?.titulo}'?") },
                confirmButton = {
                    Button(
                        onClick = {
                            libroABorrar?.let { viewModel.eliminarLibro(it) }
                            mostrarDialogoBorrado = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrado = false }) { Text("Cancelar") }
                }
            )
        }
    }
}