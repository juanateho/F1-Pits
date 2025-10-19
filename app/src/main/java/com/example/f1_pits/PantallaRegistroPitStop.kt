
package com.example.f1_pits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaRegistroPitStop(paradas: List<ParadaEnBox>) {
    Column(modifier = Modifier.padding(16.dp)) {
        LazyColumn {
            // Cabecera de la tabla
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text("ID", modifier = Modifier.weight(0.5f))
                    Text("Piloto", modifier = Modifier.weight(2f))
                    Text("Equipo", modifier = Modifier.weight(1.5f))
                    Text("Tiempo", modifier = Modifier.weight(1f))
                    Text("Acción", modifier = Modifier.weight(1f))
                }
            }

            // Filas de la tabla
            items(paradas) { parada ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(parada.id.toString(), modifier = Modifier.weight(0.5f))
                    Text(parada.nombrePiloto, modifier = Modifier.weight(2f))
                    Text(parada.equipo, modifier = Modifier.weight(1.5f))
                    Text(parada.tiempoPit.toString(), modifier = Modifier.weight(1f))
                    Button(onClick = { /* TODO: Implementar edición */ }, modifier = Modifier.weight(1f)) {
                        Text("Editar")
                    }
                }
            }
        }
    }
}
