package com.example.padelmarcheofficial.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.dataclass.GestioneFirebase


/**
 * Classe Factory utile a fornire un'istanza di PrenotazioniAdminViewModel in modo che possa essere
 * creato correttamente e istanziata la dipendenza da GestioneFirebase
 */
class PrenotazioniAdminViewModelFactory(private val gestioneFirebase: GestioneFirebase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrenotazioniAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrenotazioniAdminViewModel(gestioneFirebase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}