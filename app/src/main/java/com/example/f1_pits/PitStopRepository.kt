
package com.example.f1_pits

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PitStopRepository(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("pit_stops", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun guardarPitStop(parada: ParadaEnBox) {
        val paradas = obtenerTodosLosPitStops().toMutableList()
        paradas.add(parada)
        guardarListaDePitStops(paradas)
    }

    fun obtenerTodosLosPitStops(): List<ParadaEnBox> {
        val json = sharedPreferences.getString("pit_stops_list", null)
        return if (json != null) {
            val type = object : TypeToken<List<ParadaEnBox>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun editarPitStop(paradaEditada: ParadaEnBox) {
        val paradas = obtenerTodosLosPitStops().toMutableList()
        val index = paradas.indexOfFirst { it.id == paradaEditada.id }
        if (index != -1) {
            paradas[index] = paradaEditada
            guardarListaDePitStops(paradas)
        }
    }

    fun eliminarPitStop(id: Int) {
        val paradas = obtenerTodosLosPitStops().toMutableList()
        paradas.removeAll { it.id == id }
        guardarListaDePitStops(paradas)
    }

    fun buscarPitStop(query: String): List<ParadaEnBox> {
        val paradas = obtenerTodosLosPitStops()
        return paradas.filter {
            it.nombrePiloto.contains(query, ignoreCase = true) ||
            it.equipo.contains(query, ignoreCase = true)
        }
    }

    private fun guardarListaDePitStops(paradas: List<ParadaEnBox>) {
        val json = gson.toJson(paradas)
        sharedPreferences.edit().putString("pit_stops_list", json).apply()
    }
}
