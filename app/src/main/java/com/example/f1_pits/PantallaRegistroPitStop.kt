
package com.example.f1_pits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PantallaRegistroPitStop(
    paradas: List<ParadaEnBox>,
    onEditPitStop: (Int) -> Unit,
    onDeletePitStop: (Int) -> Unit,
    onSearchPitStop: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearchPitStop(it)
            },
            label = { Text("Buscar por piloto o equipo") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            // Cabecera de la tabla
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text("Piloto", modifier = Modifier.weight(2f))
                    Text("Equipo", modifier = Modifier.weight(1.5f))
                    Text("Tiempo", modifier = Modifier.weight(1f))
                    Text("Estado", modifier = Modifier.weight(1f))
                    Text("Acciones", modifier = Modifier.weight(1.5f))
                }
            }

            // Filas de la tabla
            items(paradas) { parada ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(parada.nombrePiloto, modifier = Modifier.weight(2f))
                    Text(parada.equipo, modifier = Modifier.weight(1.5f))
                    Text(parada.tiempoPit.toString(), modifier = Modifier.weight(1f))
                    Text(
                        text = parada.estado.displayName,
                        color = if (parada.estado == EstadoParada.OK) Color.Green else Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                    Row(modifier = Modifier.weight(1.5f)) {
                        IconButton(onClick = { onEditPitStop(parada.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { onDeletePitStop(parada.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}
