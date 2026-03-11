package com.example.bibliotecaroom

import com.example.bibliotecaroom.presentation.screens.ObtenerTodosLosLibrosScreen
import com.example.bibliotecaroom.presentation.screens.PantallaPrincipal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bibliotecaroom.domain.model.Pantalla
import com.example.bibliotecaroom.presentation.screens.AltaLibroScreen
import com.example.bibliotecaroom.presentation.screens.DetalleLibroScreen
import com.example.bibliotecaroom.presentation.screens.EdicionLibroScreen

import com.example.bibliotecaroom.presentation.viewModel.LibroViewModel
import com.example.bibliotecaroom.ui.theme.BibliotecaRoomTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BibliotecaRoomTheme {
                val navController = rememberNavController()

                // IMPORTANTE: Instanciar el ViewModel aquí para compartirlo en el NavHost
                val libroViewModel: LibroViewModel = hiltViewModel()

                NavHost(
                    navController = navController,
                    startDestination = Pantalla.Main.route
                ) {
                    // 1. Menú Principal
                    composable(Pantalla.Main.route) {
                        PantallaPrincipal(onNavigate = { route ->
                            navController.navigate(route)
                        })
                    }

                    // 2. Listado Completo
                    composable(Pantalla.GetAll.route) {
                        ObtenerTodosLosLibrosScreen(
                            viewModel = libroViewModel,
                            onBackClick = { navController.popBackStack() },
                            onNavigate = { ruta -> navController.navigate(ruta) }
                        )
                    }

                    // 3. Alta de Libro
                    composable(Pantalla.Insert.route) {
                        AltaLibroScreen(
                            viewModel = libroViewModel,
                            onVolver = { navController.popBackStack() }
                        )
                    }

                    // 4. Edición (Usa el libroSeleccionado del ViewModel)
                    composable(Pantalla.Update.route) {
                        EdicionLibroScreen(
                            viewModel = libroViewModel,
                            onVolver = { navController.popBackStack() }
                        )
                    }

                    // 5. Detalle (Usa el libroSeleccionado del ViewModel)
                    composable(Pantalla.GetById.route) {
                        DetalleLibroScreen(
                            viewModel = libroViewModel,
                            onVolver = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}