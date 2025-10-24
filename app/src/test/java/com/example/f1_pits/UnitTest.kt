package com.example.f1_pits

import org.junit.Test

import org.junit.Assert.*

class UnitTest {

    @Test
    fun testCrearObjetoParada() {
        val parada = ParadaEnBox(
            id = 1,
            nombrePiloto = "Lewis Hamilton",
            equipo = "Mercedes",
            tiempoPit = 2.5,
            neumaticos = TipoNeumatico.BLANDO,
            neumaticosCambiados = 4,
            estado = EstadoParada.OK,
            motivoFallo = null,
            fechaHora = "2024-01-01T12:00:00Z",
            mecanicoPrincipal = "Bono"
        )
        assertEquals(1, parada.id)
        assertEquals("Lewis Hamilton", parada.nombrePiloto)
        assertEquals("Mercedes", parada.equipo)
        assertEquals(2.5, parada.tiempoPit, 0.0)
        assertEquals(TipoNeumatico.BLANDO, parada.neumaticos)
        assertEquals(4, parada.neumaticosCambiados)
        assertEquals(EstadoParada.OK, parada.estado)
        assertNull(parada.motivoFallo)
        assertEquals("2024-01-01T12:00:00Z", parada.fechaHora)
        assertEquals("Bono", parada.mecanicoPrincipal)
    }

    @Test
    fun testPantallaRegistro() {

        val pilotoSeleccionado = "Max Verstappen"
        val equipoSeleccionado = "Red Bull Racing"
        val tiempoPit = "1.9"
        val neumaticos = TipoNeumatico.MEDIO
        val neumaticosCambiados = 4
        val estado = EstadoParada.OK
        val motivoFallo = ""
        val fechaHora = "2024-05-15T10:30:00Z"
        val mecanicoPrincipal = "Adrian Newey"
        val paradaAEditar: ParadaEnBox? = null

        val paradaGuardada = (paradaAEditar ?: ParadaEnBox(0, "", "", 0.0, TipoNeumatico.BLANDO, 0, EstadoParada.OK, null, "", "")).copy(
            nombrePiloto = pilotoSeleccionado,
            equipo = equipoSeleccionado,
            tiempoPit = tiempoPit.toDoubleOrNull() ?: 0.0,
            neumaticos = neumaticos,
            neumaticosCambiados = neumaticosCambiados,
            estado = estado,
            motivoFallo = if (estado == EstadoParada.FALLIDO) motivoFallo else null,
            fechaHora = if(paradaAEditar != null) paradaAEditar.fechaHora else fechaHora,
            mecanicoPrincipal = mecanicoPrincipal
        )

        assertEquals("Max Verstappen", paradaGuardada.nombrePiloto)
        assertEquals("Red Bull Racing", paradaGuardada.equipo)
        assertEquals(1.9, paradaGuardada.tiempoPit, 0.0)
        assertEquals(TipoNeumatico.MEDIO, paradaGuardada.neumaticos)
        assertEquals(4, paradaGuardada.neumaticosCambiados)
        assertEquals(EstadoParada.OK, paradaGuardada.estado)
        assertNull(paradaGuardada.motivoFallo)
        assertEquals("2024-05-15T10:30:00Z", paradaGuardada.fechaHora)
        assertEquals("Adrian Newey", paradaGuardada.mecanicoPrincipal)
        assertEquals(0, paradaGuardada.id)
    }

    @Test
    fun testPantallaResumen() {
        val paradas = listOf(
            ParadaEnBox(1, "Lando Norris", "McLaren", 2.1, TipoNeumatico.DURO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(2, "Charles Leclerc", "Ferrari", 3.2, TipoNeumatico.MEDIO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(3, "Max Verstappen", "Red Bull Racing", 1.9, TipoNeumatico.BLANDO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(4, "Fernando Alonso", "Aston Martin", 2.8, TipoNeumatico.MEDIO, 4, EstadoParada.OK, null, "", "")
        )

        val fastestPit = paradas.minByOrNull { it.tiempoPit }?.tiempoPit
        val averageTime = paradas.map { it.tiempoPit }.average()

        assertEquals(1.9, fastestPit!!, 0.0)
        assertEquals(2.5, averageTime, 0.001)
    }

    @Test
    fun testPantallaLista() {
        val paradas = listOf(
            ParadaEnBox(1, "Lando Norris", "McLaren", 2.1, TipoNeumatico.DURO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(2, "Charles Leclerc", "Ferrari", 3.2, TipoNeumatico.MEDIO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(3, "Max Verstappen", "Red Bull Racing", 1.9, TipoNeumatico.BLANDO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(4, "Sergio Pérez", "Red Bull Racing", 2.2, TipoNeumatico.BLANDO, 4, EstadoParada.OK, null, "", ""),
            ParadaEnBox(5, "Fernando Alonso", "Aston Martin", 2.8, TipoNeumatico.MEDIO, 4, EstadoParada.OK, null, "", "")
        )

        // Test 1: Busqueda por equipo
        val searchQueryTeam = "Red Bull"
        val onSearchPitStopResultTeam = paradas.filter {
            it.nombrePiloto.contains(searchQueryTeam, ignoreCase = true) || it.equipo.contains(searchQueryTeam, ignoreCase = true)
        }
        assertEquals(2, onSearchPitStopResultTeam.size)
        assertTrue(onSearchPitStopResultTeam.any { it.nombrePiloto == "Max Verstappen" })
        assertTrue(onSearchPitStopResultTeam.any { it.nombrePiloto == "Sergio Pérez" })


        // Test 2: Busqueda por piloto
        val searchQueryPilot = "Lando"
        val onSearchPitStopResultPilot = paradas.filter {
            it.nombrePiloto.contains(searchQueryPilot, ignoreCase = true) || it.equipo.contains(searchQueryPilot, ignoreCase = true)
        }
        assertEquals(1, onSearchPitStopResultPilot.size)
        assertEquals("Lando Norris", onSearchPitStopResultPilot.first().nombrePiloto)

        // Test 3: Sin resultado
        val searchQueryNone = "Haas"
        val onSearchPitStopResultNone = paradas.filter {
            it.nombrePiloto.contains(searchQueryNone, ignoreCase = true) || it.equipo.contains(searchQueryNone, ignoreCase = true)
        }
        assertEquals(0, onSearchPitStopResultNone.size)
    }
}