
package com.example.f1_pits

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroPitStop(
    paradaAEditar: ParadaEnBox?,
    onSavePitStop: (ParadaEnBox) -> Unit,
    onCancel: () -> Unit
) {
    val equipos = mapOf(
        "Mercedes" to listOf("Lewis Hamilton", "George Russell"),
        "Red Bull Racing" to listOf("Max Verstappen", "Sergio Pérez"),
        "Ferrari" to listOf("Charles Leclerc", "Carlos Sainz Jr."),
        "McLaren" to listOf("Lando Norris", "Oscar Piastri"),
        "Aston Martin" to listOf("Fernando Alonso", "Lance Stroll"),
        "Alpine" to listOf("Esteban Ocon", "Pierre Gasly"),
        "Williams" to listOf("Alex Albon", "Logan Sargeant"),
        "RB" to listOf("Yuki Tsunoda", "Daniel Ricciardo"),
        "Sauber" to listOf("Valtteri Bottas", "Zhou Guanyu"),
        "Haas" to listOf("Nico Hülkenberg", "Kevin Magnussen"),
    )

    val mecanicosPrincipalesPorEscuderia = mapOf(
        "Mercedes" to "Andrew Shovlin",
        "Red Bull Racing" to "Adrian Newey",
        "Ferrari" to "Jock Clear",
        "McLaren" to "Andrea Stella",
        "Aston Martin" to "Dan Fallows",
        "Alpine" to "Matt Harman",
        "Williams" to "Sven Smeets",
        "RB" to "Jody Egginton",
        "Sauber" to "James Key",
        "Haas" to "Simone Resta",
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
    var expandedNumNeumaticos by remember { mutableStateOf(false) }
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

    LaunchedEffect(paradaAEditar == null) {
        if (paradaAEditar == null) {
            while (true) {
                fechaHora = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                delay(1000)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (paradaAEditar == null) "Registrar Pit Stop" else "Editar Pit Stop",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Escudería
        CustomOutlinedDropdown(label = "ESCUDERÍA", selectedItem = equipoSeleccionado, items = equipos.keys.toList(), onItemSelected = { 
            equipoSeleccionado = it
            pilotoSeleccionado = null // Reset driver selection
            mecanicoPrincipal = mecanicosPrincipalesPorEscuderia[it] ?: ""
         }, expanded = expandedEquipo, onExpandedChange = { expandedEquipo = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Piloto
        CustomOutlinedDropdown(label = "PILOTO", selectedItem = pilotoSeleccionado, items = equipos[equipoSeleccionado] ?: emptyList(), onItemSelected = { pilotoSeleccionado = it }, expanded = expandedPiloto, onExpandedChange = { expandedPiloto = it }, enabled = equipoSeleccionado != null)
        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedTextField(
            value = tiempoPit,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() || it == '.' }
                if (filtered.count { it == '.' } <= 1) {
                    tiempoPit = filtered
                }
            },
            label = "TIEMPO TOTAL (S)",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Cambio de Neumáticos
        Text(text = "CAMBIO DE NEUMÁTICOS", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val neumaticoAImagen = mapOf(
                TipoNeumatico.BLANDO to R.drawable.soft,
                TipoNeumatico.MEDIO to R.drawable.medium,
                TipoNeumatico.DURO to R.drawable.hard,
                TipoNeumatico.INTERMEDIO to R.drawable.intermediate,
                TipoNeumatico.LLUVIA to R.drawable.wet
            )

            TipoNeumatico.values().forEach { tipo ->
                val imagenId = neumaticoAImagen[tipo]!!
                AnimatableImagenRadioButton(
                    selected = neumaticos == tipo,
                    onClick = { neumaticos = tipo },
                    imagenId = imagenId,
                    displayName = tipo.displayName,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Numero de Neumáticos y Estado
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)){
            CustomOutlinedDropdown(label = "NUMERO DE NEUMÁTICOS CAMBIADOS", selectedItem = neumaticosCambiados.toString(), items = (0..4).map { it.toString() }, onItemSelected = { neumaticosCambiados = it.toInt() }, expanded = expandedNumNeumaticos, onExpandedChange = { expandedNumNeumaticos = it }, modifier = Modifier.weight(1f))
            CustomOutlinedDropdown(label = "ESTADO", selectedItem = estado.displayName, items = EstadoParada.values().map { it.displayName }, onItemSelected = { selectedName -> estado = EstadoParada.values().first { it.displayName == selectedName }}, expanded = expandedEstado, onExpandedChange = { expandedEstado = it }, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (estado == EstadoParada.FALLIDO) {
            CustomOutlinedTextField(value = motivoFallo, onValueChange = { motivoFallo = it }, label = "MOTIVO DEL FALLO")
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Mecánico y Fecha
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)){
            CustomOutlinedTextField(value = mecanicoPrincipal, onValueChange = {}, label = "MECÁNICO PRINCIPAL", readOnly = true, modifier = Modifier.weight(1f))
            CustomOutlinedTextField(value = fechaHora, onValueChange = {}, label = "FECHA Y HORA DEL PIT STOP", readOnly = true, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { 
                val paradaGuardada = (paradaAEditar ?: ParadaEnBox(0, "", "", 0.0, TipoNeumatico.BLANDO, 0, EstadoParada.OK, null, "", "")).copy(
                    nombrePiloto = pilotoSeleccionado ?: "",
                    equipo = equipoSeleccionado ?: "",
                    tiempoPit = tiempoPit.toDoubleOrNull() ?: 0.0,
                    neumaticos = neumaticos,
                    neumaticosCambiados = neumaticosCambiados,
                    estado = estado,
                    motivoFallo = if (estado == EstadoParada.FALLIDO) motivoFallo else null,
                    fechaHora = if(paradaAEditar != null) paradaAEditar.fechaHora else fechaHora,
                    mecanicoPrincipal = mecanicoPrincipal
                )
                onSavePitStop(paradaGuardada) 
            }, modifier = Modifier.weight(1f).height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)), shape = RoundedCornerShape(12.dp)) {
                Text(if (paradaAEditar == null) "GUARDAR" else "ACTUALIZAR", color = Color.White)
            }
            Button(onClick = onCancel, modifier = Modifier.weight(1f).height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)), shape = RoundedCornerShape(12.dp)) {
                Text("CANCELAR", color = Color.White)
            }
        }
    }
}

@Composable
fun AnimatableImagenRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    imagenId: Int,
    displayName: String,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smoke))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = selected,
        restartOnPlay = true,
        iterations = 1
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(70.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onClick, modifier = Modifier.size(52.dp)) {
                Image(
                    painter = painterResource(id = imagenId),
                    contentDescription = displayName,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            if (progress > 0f && progress < 1f) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(120.dp)
                )
            }
        }
        Text(text = displayName, fontSize = 12.sp)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedDropdown(
    label: String,
    selectedItem: String?,
    items: List<String>,
    onItemSelected: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
            TextField(
                value = selectedItem ?: "",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                enabled = enabled
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                items.forEach { item ->
                    DropdownMenuItem(text = { Text(item) }, onClick = {
                        onItemSelected(item)
                        onExpandedChange(false)
                    })
                }
            }
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}
