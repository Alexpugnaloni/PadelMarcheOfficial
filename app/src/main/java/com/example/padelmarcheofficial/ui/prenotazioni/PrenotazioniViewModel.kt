package com.example.padelmarcheofficial.ui.prenotazioni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.Prenotazione
import com.example.padelmarcheofficial.dataclass.UserValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrenotazioniViewModel(private val gestioneFirebase: GestioneFirebase) : ViewModel() {
    private val _prenotazioniUtente = MutableLiveData<List<Prenotazione>>()
    val prenotazioniUtente: LiveData<List<Prenotazione>> get() = _prenotazioniUtente

    fun fetchPrenotazioniUtente(){
        viewModelScope.launch(Dispatchers.IO){
            val prenotazioni = gestioneFirebase.downloadPrenotazioniUtente()
            _prenotazioniUtente.postValue(prenotazioni)
        }
    }
}