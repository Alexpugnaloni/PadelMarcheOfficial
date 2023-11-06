package com.example.padelmarcheofficial.dataclass

import java.util.Date

data class UtenteRegistrato(
    val nome: String,
    val cognome: String,
    val email: String,
    val numeroTelefono: String
)

data class CentroSportivo(
    val id: String,
    val nome: String,
    val indirizzo: String,
    val civico: String
)


data class Prenotazione(
    val id: String,
    val utente: String,//UtenteRegistrato,
    val centroSportivo: String,//CentroSportivo,
    val date: Date,
    val listautenti: List<String>, //Utenti che si prenotano mano a mano
)