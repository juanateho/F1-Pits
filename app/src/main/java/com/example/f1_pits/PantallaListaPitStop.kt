
package com.example.f1_pits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaListaPitStop(
    paradaAEditar: ParadaEnBox?,
    onSavePitStop: (ParadaEnBox) -> Unit
) {
    var nombrePiloto by remember { mutableStateOf("") }
    var equipo by remember { mutableStateOf("") }
    var tiempoPit by remember { mutableStateOf("") }
    var neumaticos by remember { mutableStateOf(TipoNeumatico.BLANDO) }
    var neumaticosCambiados by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf(EstadoParada.OK) }
    var motivoFallo by remember { mutableStateOf("") }

    LaunchedEffect(paradaAEditar) {
        if (paradaAEditar != null) {
            nombrePiloto = paradaAEditar.nombrePiloto
            equipo = paradaAEditar.equipo
            tiempoPit = paradaAEditar.tiempoPit.toString()
            neumaticos = paradaAEditar.neumaticos
            neumaticosCambiados = paradaAEditar.neumaticosCambiados.toString()
            estado = paradaAEditar.estado
            motivoFallo = paradaAEditar.motivoFallo ?: ""
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = nombrePiloto, onValueChange = { nombrePiloto = it }, label = { Text("Nombre del Piloto") })
        TextField(value = equipo, onValueChange = { equipo = it }, label = { Text("Equipo") })
        TextField(value = tiempoPit, onValueChange = { tiempoPit = it }, label = { Text("Tiempo de Pit (s)") })
        TextField(value = neumaticosCambiados, onValueChange = { neumaticosCambiados = it }, label = { Text("Neumáticos Cambiados") })

        Text("Neumáticos:")
        Row {
            TipoNeumatico.values().forEach { tipoNeumatico ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = neumaticos == tipoNeumatico, onClick = { neumaticos = tipoNeumatico })
                    Text(text = tipoNeumatico.displayName)
                }
            }
        }

        Text("Estado:")
        Row {
            EstadoParada.values().forEach { estadoParada ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = estado == estadoParada, onClick = { estado = estadoParada })
                    Text(text = estadoParada.displayName)
                }
            }
        }

        if (estado == EstadoParada.FALLIDO) {
            TextField(value = motivoFallo, onValueChange = { motivoFallo = it }, label = { Text("Motivo del Fallo") })
        }

        Button(onClick = {
            val paradaGuardada = (paradaAEditar ?: ParadaEnBox(0, "", "", 0.0, TipoNeumatico.BLANDO, 0, EstadoParada.OK, null, "")).copy(
                nombrePiloto = nombrePiloto,
                equipo = equipo,
                tiempoPit = tiempoPit.toDoubleOrNull() ?: 0.0,
                neumaticos = neumaticos,
                neumaticosCambiados = neumaticosCambiados.toIntOrNull() ?: 0,
                estado = estado,
                motivoFallo = if (estado == EstadoParada.FALLIDO) motivoFallo else null,
                fechaHora = paradaAEditar?.fechaHora ?: SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            )
            onSavePitStop(paradaGuardada)
        }) {
            Text(if (paradaAEditar == null) "Registrar Pit Stop" else "Actualizar Pit Stop")
        }
    }
}
