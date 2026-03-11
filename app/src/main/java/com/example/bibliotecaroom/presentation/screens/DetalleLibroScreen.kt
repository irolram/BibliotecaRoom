package com.example.bibliotecaroom.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLibroScreen(
    onVolver: () -> Unit,
    viewModel: LibroViewModel
) {
    // Obtenemos el libro que guardamos en el ViewModel antes de navegar
    val libro = viewModel.libroSeleccionado

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Libro") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (libro != null) {
                // Tarjeta con la información detallada
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        DetalleCampo(label = "Título", valor = libro.titulo, isHeader = true)
                        DetalleCampo(label = "Autor", valor = libro.autor)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        DetalleCampo(label = "Género", valor = libro.genero)
                        DetalleCampo(label = "Editorial", valor = libro.editorial)
                        DetalleCampo(label = "Año de Publicación", valor = libro.anioPublicacion.toString())
                        DetalleCampo(label = "Número de Páginas", valor = libro.paginas.toString())

                        Spacer(modifier = Modifier.height(16.dp))

                        // Estado de disponibilidad con color dinámico
                        val colorEstado = if (libro.disponible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        Text(
                            text = if (libro.disponible) "ESTADO: DISPONIBLE" else "ESTADO: PRESTADO",
                            color = colorEstado,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Listado")
                }
            } else {
                // Caso de error: no hay libro seleccionado
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se han encontrado datos del libro.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

/**
 * Componente auxiliar para mostrar cada fila de detalle
 */
@Composable
fun DetalleCampo(label: String, valor: String, isHeader: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = valor,
            style = if (isHeader) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal
        )
    }
}