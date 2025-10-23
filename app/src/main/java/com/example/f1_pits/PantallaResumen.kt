
package com.example.f1_pits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PantallaResumenPitStop(
    navController: NavController,
    totalPits: Int,
    fastestPit: Double?,
    averageTime: Double,
    paradas: List<ParadaEnBox>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Resumen de Pit Stops",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DirectionsCar, contentDescription = "Pit stop más rápido", tint = Color(0xFF6A1B9A))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Pit stop más rápido: ${fastestPit?.let { "${it}s" } ?: "N/A"}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AvTimer, contentDescription = "Promedio de tiempos", tint = Color(0xFF6A1B9A))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Promedio de tiempos: ${if (averageTime > 0) "%.2fs".format(averageTime) else "N/A"}")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = "Total de paradas", tint = Color(0xFF6A1B9A))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Total de paradas: $totalPits")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        EscuderiaBarChart(paradas = paradas)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("registro") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
        ) {
            Text(text = "Registrar Pit Stop", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("lista") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333))
        ) {
            Text(text = "Ver Listado", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun EscuderiaBarChart(paradas: List<ParadaEnBox>) {
    val teamColors = mapOf(
        "Mercedes" to Color(0xFF00D7B6),
        "Red Bull Racing" to Color(0xFF4781D7),
        "Ferrari" to Color(0xFFED1131),
        "McLaren" to Color(0xFFF47600),
        "Alpine" to Color(0xFF00A1E8),
        "RB" to Color(0xFF6C98FF),
        "Aston Martin" to Color(0xFF229971),
        "Williams" to Color(0xFF1868DB),
        "Sauber" to Color(0xFF01C00E),
        "Haas" to Color(0xFF9C9FA2)
    )

    val teamTimes = paradas.groupBy { it.equipo }
        .mapValues { entry -> entry.value.sumOf { it.tiempoPit } }

    val maxTime = teamTimes.values.maxOrNull() ?: 1.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text("Tiempos totales por escudería", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        teamTimes.forEach { (team, time) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(team, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth((time / maxTime).toFloat())
                            .height(20.dp)
                            .background(teamColors[team] ?: Color.Gray)
                    )
                }
                Text(String.format("%.2f", time), modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
