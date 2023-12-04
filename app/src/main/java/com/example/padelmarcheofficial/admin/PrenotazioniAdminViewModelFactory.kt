package com.example.padelmarcheofficial.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioniViewModel

class PrenotazioniAdminViewModelFactory(private val gestioneFirebase: GestioneFirebase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrenotazioniAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrenotazioniAdminViewModel(gestioneFirebase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}