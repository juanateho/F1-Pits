
package com.example.f1_pits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.f1_pits.ui.theme.F1PitsTheme

@Composable
fun PantallaResumenPitStop(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Resumen de Pit Stops")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Pit Stop más rápido: 2.5s")
        Text(text = "Tiempo promedio de Pit Stop: 3.2s")
        Text(text = "Total de Pit Stops: 10")
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { navController.navigate("lista") }) {
                Text(text = "Registrar Pit Stop")
            }
            Button(onClick = { navController.navigate("registro") }) {
                Text(text = "Ver Pit Stops")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaResumenPitStopPreview() {
    F1PitsTheme {
        PantallaResumenPitStop(navController = rememberNavController())
    }
}
