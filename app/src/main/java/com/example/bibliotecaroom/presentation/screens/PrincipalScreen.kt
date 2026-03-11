package com.example.bibliotecaroom.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.bibliotecaroom.domain.model.Libro
import com.example.bibliotecaroom.domain.model.Pantalla
import com.example.bibliotecaroom.presentation.components.ItemLibro
import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel

@Composable
fun PantallaPrincipal(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Gestión de Biblioteca",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = { onNavigate(Pantalla.GetAll.route) },
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(bottom = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Default.List, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text("Listar todos los libros")
        }

        // Opción 2: Alta
        Button(
            onClick = { onNavigate(Pantalla.Insert.route) },
            modifier = Modifier.fillMaxWidth().height(80.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text("Dar de alta nuevo libro")
        }
    }
}


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

            // --- BUSCADOR POR ID ---
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

        // Diálogo de confirmación (se mantiene igual)
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