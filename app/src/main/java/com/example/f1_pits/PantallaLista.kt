
package com.example.f1_pits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaListaPitStop(
    paradas: List<ParadaEnBox>,
    onEditPitStop: (Int) -> Unit,
    onDeletePitStop: (Int) -> Unit,
    onSearchPitStop: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Listado de Pit Stops",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearchPitStop(it)
            },
            placeholder = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Icono de búsqueda") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Piloto", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                        Text("Escudería", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
                        Text("Tiempo (s)", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Estado", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Acciones", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }

                items(paradas) { parada ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(parada.nombrePiloto, modifier = Modifier.weight(2f))
                        Text(parada.equipo, modifier = Modifier.weight(1.5f))
                        Text(parada.tiempoPit.toString(), modifier = Modifier.weight(1f))
                        Text(
                            text = parada.estado.displayName,
                            color = if (parada.estado == EstadoParada.OK) Color(0xFF2E7D32) else Color(0xFFC62828),
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (parada.estado == EstadoParada.OK) Color(0xFFC8E6C9) else Color(0xFFFFCDD2),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            textAlign = TextAlign.Center
                        )
                        Row(modifier = Modifier.weight(1.5f), horizontalArrangement = Arrangement.Center) {
                            IconButton(onClick = { onEditPitStop(parada.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF6A1B9A))
                            }
                            IconButton(onClick = { onDeletePitStop(parada.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}
