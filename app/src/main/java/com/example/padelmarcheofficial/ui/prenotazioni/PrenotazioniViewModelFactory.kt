package com.example.padelmarcheofficial.ui.prenotazioni

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.dataclass.GestioneFirebase

class PrenotazioniViewModelFactory(private val gestioneFirebase: GestioneFirebase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrenotazioniViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrenotazioniViewModel(gestioneFirebase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

