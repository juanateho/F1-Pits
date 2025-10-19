package com.example.f1_pits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            PantallaResumenPitStop(navController = navController)
        }
        composable("lista") {
            PantallaListaPitStop(onAddPitStop = { nuevaParada ->
                val paradaConId = nuevaParada.copy(id = (paradasEnBox.value.maxOfOrNull { it.id } ?: 0) + 1)
                paradasEnBox.value = paradasEnBox.value + paradaConId
                navController.navigate("registro") {
                    popUpTo("lista") { inclusive = true }
                }
            })
        }
        composable("registro") {
            PantallaRegistroPitStop(paradas = paradasEnBox.value)
        }
    }
}