package com.example.padelmarcheofficial.dataclass

import java.util.Date

data class CentroSportivo(
    val id: String,
    val nome: String,
    val indirizzo: String,
    val civico: String,
    val email: String
)


data class Prenotazione(
    val id: String,
    val utente: String,
    val centroSportivo: String,
    val date: Date,
    val listautenti: List<String>,
    val confermato: Boolean,
)


data class PrenotazioneAdmin(
    val id: String,
    val utente: String,
    val nomeutente: String,
    val centroSportivo: String,
    val date: Date,
    val listautenti: List<String>,
    val cellulareUtente: String
)