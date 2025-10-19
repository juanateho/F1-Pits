package com.example.f1_pits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val paradasEnBox = remember {
        mutableStateOf(
            listOf(
                ParadaEnBox(1, "Lando Norris", "McLaren", 1.8, TipoNeumatico.BLANDO, 4, EstadoParada.OK, null, "2024-01-01 12:00:00"),
                ParadaEnBox(2, "Max Verstappen", "Red Bull", 2.1, TipoNeumatico.MEDIO, 4, EstadoParada.OK, null, "2024-01-01 12:05:00")
            )
        )
    }

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
        composable(
            "lista?pitStopId={pitStopId}",
            arguments = listOf(navArgument("pitStopId") { 
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val pitStopId = backStackEntry.arguments?.getString("pitStopId")?.toIntOrNull()
            val paradaAEditar = paradasEnBox.value.find { it.id == pitStopId }

            PantallaListaPitStop(
                paradaAEditar = paradaAEditar,
                onSavePitStop = { parada ->
                    if (paradaAEditar == null) { // Creando
                        val paradaConId = parada.copy(id = (paradasEnBox.value.maxOfOrNull { it.id } ?: 0) + 1)
                        paradasEnBox.value = paradasEnBox.value + paradaConId
                    } else { // Editando
                        paradasEnBox.value = paradasEnBox.value.map { if (it.id == parada.id) parada else it }
                    }
                    navController.popBackStack()
                }
            )
        }
        composable("registro") {
            PantallaRegistroPitStop(
                paradas = paradasEnBox.value,
                onEditPitStop = { pitStopId ->
                    navController.navigate("lista?pitStopId=$pitStopId")
                },
                onDeletePitStop = { pitStopId ->
                    paradasEnBox.value = paradasEnBox.value.filter { it.id != pitStopId }
                }
            )
        }
    }
}