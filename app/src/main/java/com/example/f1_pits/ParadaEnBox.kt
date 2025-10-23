
package com.example.f1_pits

data class ParadaEnBox(
    val id: Int,
    val nombrePiloto: String,
    val equipo: String,
    val tiempoPit: Double,
    val neumaticos: TipoNeumatico,
    val neumaticosCambiados: Int,
    val estado: EstadoParada,
    val motivoFallo: String?,
    val fechaHora: String,
    val mecanicoPrincipal: String
)

enum class TipoNeumatico(val displayName: String) {
    BLANDO("Blandos"),
    MEDIO("Medios"),
    DURO("Duros"),
    INTERMEDIO("Intermedios"),
    LLUVIA("Lluvia")
}

enum class EstadoParada(val displayName: String) {
    OK("OK"),
    FALLIDO("Fallido")
}
