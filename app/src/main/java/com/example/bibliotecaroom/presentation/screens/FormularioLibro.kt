package com.example.bibliotecaroom.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Composable para el formulario de edición de un libro
@Composable
fun FormularioLibro(
    tituloInicial: String = "",
    autorInicial: String = "",
    anioInicial: String = "",
    generoInicial: String = "",
    editorialInicial: String = "",
    paginasInicial: String = "",
    disponibleInicial: Boolean = true,
    textoBoton: String,
    onGuardarClick: (String, String, Int, String, String, Int, Boolean) -> Unit
) {
    // Al añadir (tituloInicial, etc.) como clave,
    // si el libro tarda 1 segundo en cargar, el campo se actualizará solo.
    var titulo by remember(tituloInicial) { mutableStateOf(tituloInicial) }
    var autor by remember(autorInicial) { mutableStateOf(autorInicial) }
    var anio by remember(anioInicial) { mutableStateOf(anioInicial) }
    var genero by remember(generoInicial) { mutableStateOf(generoInicial) }
    var editorial by remember(editorialInicial) { mutableStateOf(editorialInicial) }
    var paginas by remember(paginasInicial) { mutableStateOf(paginasInicial) }
    var disponible by remember(disponibleInicial) { mutableStateOf(disponibleInicial) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Por si hay muchos campos
    ) {
        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = autor, onValueChange = { autor = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = genero, onValueChange = { genero = it }, label = { Text("Género") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = editorial, onValueChange = { editorial = it }, label = { Text("Editorial") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = anio,
            onValueChange = { anio = it },
            label = { Text("Año") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = paginas,
            onValueChange = { paginas = it },
            label = { Text("Nº Páginas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = disponible, onCheckedChange = { disponible = it })
            Text("Disponible para préstamo")
        }

        Button(
            onClick = {
                onGuardarClick(
                    titulo, autor, anio.toIntOrNull() ?: 0,
                    genero, editorial, paginas.toIntOrNull() ?: 0, disponible
                )
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            enabled = titulo.isNotBlank() && autor.isNotBlank()
        ) {
            Text(textoBoton)
        }
    }
}