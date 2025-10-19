
package com.example.f1_pits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaPitStop(
    paradaAEditar: ParadaEnBox?,
    onSavePitStop: (ParadaEnBox) -> Unit
) {
    val equipos = mapOf(
        "McLaren" to listOf("Lando Norris", "Oscar Piastri"),
        "Red Bull" to listOf("Max Verstappen", "Sergio Pérez"),
        "Ferrari" to listOf("Charles Leclerc", "Carlos Sainz Jr.")
    )

    var equipoSeleccionado by remember { mutableStateOf<String?>(null) }
    var pilotoSeleccionado by remember { mutableStateOf<String?>(null) }
    var tiempoPit by remember { mutableStateOf("") }
    var neumaticos by remember { mutableStateOf(TipoNeumatico.BLANDO) }
    var neumaticosCambiados by remember { mutableStateOf(4) }
    var estado by remember { mutableStateOf(EstadoParada.OK) }
    var motivoFallo by remember { mutableStateOf("") }
    var mecanicoPrincipal by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") }

    var expandedEquipo by remember { mutableStateOf(false) }
    var expandedPiloto by remember { mutableStateOf(false) }
    var expandedNeumaticos by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }

    fun limpiarCampos() {
        equipoSeleccionado = null
        pilotoSeleccionado = null
        tiempoPit = ""
        neumaticos = TipoNeumatico.BLANDO
        neumaticosCambiados = 4
        estado = EstadoParada.OK
        motivoFallo = ""
        mecanicoPrincipal = ""
        fechaHora = ""
    }

    LaunchedEffect(paradaAEditar) {
        if (paradaAEditar != null) {
            equipoSeleccionado = paradaAEditar.equipo
            pilotoSeleccionado = paradaAEditar.nombrePiloto
            tiempoPit = paradaAEditar.tiempoPit.toString()
            neumaticos = paradaAEditar.neumaticos
            neumaticosCambiados = paradaAEditar.neumaticosCambiados
            estado = paradaAEditar.estado
            motivoFallo = paradaAEditar.motivoFallo ?: ""
            mecanicoPrincipal = paradaAEditar.mecanicoPrincipal
            fechaHora = paradaAEditar.fechaHora
        } else {
            limpiarCampos()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Equipo
        ExposedDropdownMenuBox(expanded = expandedEquipo, onExpandedChange = { expandedEquipo = !expandedEquipo }) {
            TextField(
                value = equipoSeleccionado ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Escudería") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEquipo) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedEquipo, onDismissRequest = { expandedEquipo = false }) {
                equipos.keys.forEach { equipo ->
                    DropdownMenuItem(text = { Text(equipo) }, onClick = {
                        equipoSeleccionado = equipo
                        pilotoSeleccionado = null // Reset driver selection
                        expandedEquipo = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Piloto
        ExposedDropdownMenuBox(expanded = expandedPiloto, onExpandedChange = { expandedPiloto = !expandedPiloto }) {
            TextField(
                value = pilotoSeleccionado ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Piloto") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPiloto) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = equipoSeleccionado != null
            )
            ExposedDropdownMenu(expanded = expandedPiloto, onDismissRequest = { expandedPiloto = false }) {
                equipos[equipoSeleccionado]?.forEach { piloto ->
                    DropdownMenuItem(text = { Text(piloto) }, onClick = {
                        pilotoSeleccionado = piloto
                        expandedPiloto = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = tiempoPit, onValueChange = { tiempoPit = it }, label = { Text("Tiempo total") }, placeholder = { Text("0.000") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Text("Cambio de neumáticos:")
        Row(modifier = Modifier.fillMaxWidth()) {
            TipoNeumatico.values().forEach { tipo ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(selected = neumaticos == tipo, onClick = { neumaticos = tipo })
                    Text(text = tipo.displayName)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Neumaticos Cambiados y Estado en la misma fila
        Row(modifier=Modifier.fillMaxWidth()){
            ExposedDropdownMenuBox(expanded = expandedNeumaticos, onExpandedChange = { expandedNeumaticos = !expandedNeumaticos }, modifier=Modifier.weight(1f)) {
                TextField(value = neumaticosCambiados.toString(), onValueChange = {}, readOnly = true, label = { Text("Neumáticos") }, modifier = Modifier.menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedNeumaticos) })
                ExposedDropdownMenu(expanded = expandedNeumaticos, onDismissRequest = { expandedNeumaticos = false }) {
                    (1..4).forEach { num ->
                        DropdownMenuItem(text = { Text(num.toString()) }, onClick = {
                            neumaticosCambiados = num
                            expandedNeumaticos = false
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            ExposedDropdownMenuBox(expanded = expandedEstado, onExpandedChange = { expandedEstado = !expandedEstado }, modifier=Modifier.weight(1f)) {
                TextField(value = estado.displayName, onValueChange = {}, readOnly = true, label = { Text("Estado") }, modifier = Modifier.menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) })
                ExposedDropdownMenu(expanded = expandedEstado, onDismissRequest = { expandedEstado = false }) {
                    EstadoParada.values().forEach { est ->
                        DropdownMenuItem(text = { Text(est.displayName) }, onClick = {
                            estado = est
                            expandedEstado = false
                        })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))


        if (estado == EstadoParada.FALLIDO) {
            TextField(value = motivoFallo, onValueChange = { motivoFallo = it }, label = { Text("Motivo del fallo") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        TextField(value = mecanicoPrincipal, onValueChange = { mecanicoPrincipal = it }, label = { Text("Mecánico principal") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = fechaHora, onValueChange = { fechaHora = it }, label = { Text("Fecha y hora del pit stop") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                val paradaGuardada = (paradaAEditar ?: ParadaEnBox(0, "", "", 0.0, TipoNeumatico.BLANDO, 0, EstadoParada.OK, null, "", "")).copy(
                    nombrePiloto = pilotoSeleccionado ?: "",
                    equipo = equipoSeleccionado ?: "",
                    tiempoPit = tiempoPit.toDoubleOrNull() ?: 0.0,
                    neumaticos = neumaticos,
                    neumaticosCambiados = neumaticosCambiados,
                    estado = estado,
                    motivoFallo = if (estado == EstadoParada.FALLIDO) motivoFallo else null,
                    fechaHora = if(fechaHora.isBlank()) SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()) else fechaHora,
                    mecanicoPrincipal = mecanicoPrincipal
                )
                onSavePitStop(paradaGuardada)
            }, modifier = Modifier.weight(1f)) {
                Text(if (paradaAEditar == null) "Guardar Pit" else "Actualizar Pit")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { limpiarCampos() }, modifier = Modifier.weight(1f)) {
                Text("Limpiar Campos")
            }
        }
    }
}
