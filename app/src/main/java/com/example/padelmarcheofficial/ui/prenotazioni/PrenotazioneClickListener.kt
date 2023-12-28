package com.example.padelmarcheofficial.ui.prenotazioni

/**
 * Interfaccia che consente la gestione delle azioni di click all'interno del PrenotazioniAdapter e
 * PrenotazioniAdminAdapter per l'eliminazione della singola prenotazione
 */
interface PrenotazioneClickListener {
    fun onPrenotazioneDelete(prenotazioneId: String)
}