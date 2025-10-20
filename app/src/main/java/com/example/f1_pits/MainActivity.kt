package com.example.f1_pits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val pitStopRepository = remember { PitStopRepository(context) }
    val paradasEnBox = remember { mutableStateOf(pitStopRepository.obtenerTodosLosPitStops()) }

    NavHost(navController = navController, startDestination = "resumen") {
        composable("resumen") {
            val totalPits = paradasEnBox.value.size
            val fastestPit = paradasEnBox.value.minOfOrNull { it.tiempoPit }
            val averageTime = paradasEnBox.value.map { it.tiempoPit }.average()

            PantallaResumenPitStop(
                navController = navController,
                totalPits = totalPits,
                fastestPit = fastestPit,
                averageTime = if (averageTime.isNaN()) 0.0 else averageTime
            )
        }
        composable("lista") {
            PantallaListaPitStop(
                paradas = paradasEnBox.value,
                onEditPitStop = { pitStopId ->
                    navController.navigate("registro?pitStopId=$pitStopId")
                },
                onDeletePitStop = { pitStopId ->
                    pitStopRepository.eliminarPitStop(pitStopId)
                    paradasEnBox.value = pitStopRepository.obtenerTodosLosPitStops()
                },
                onSearchPitStop = { query ->
                    paradasEnBox.value = if (query.isBlank()) {
                        pitStopRepository.obtenerTodosLosPitStops()
                    } else {
                        pitStopRepository.buscarPitStop(query)
                    }
                }
            )
        }
        composable(
            "registro?pitStopId={pitStopId}",
            arguments = listOf(navArgument("pitStopId") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val pitStopId = backStackEntry.arguments?.getString("pitStopId")?.toIntOrNull()
            val paradaAEditar = paradasEnBox.value.find { it.id == pitStopId }

            PantallaRegistroPitStop(
                paradaAEditar = paradaAEditar,
                onSavePitStop = { parada ->
                    if (paradaAEditar == null) { // Creando
                        val newId = (paradasEnBox.value.maxOfOrNull { it.id } ?: 0) + 1
                        val paradaConId = parada.copy(id = newId)
                        pitStopRepository.guardarPitStop(paradaConId)
                    } else { // Editando
                        pitStopRepository.editarPitStop(parada)
                    }
                    paradasEnBox.value = pitStopRepository.obtenerTodosLosPitStops()
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
