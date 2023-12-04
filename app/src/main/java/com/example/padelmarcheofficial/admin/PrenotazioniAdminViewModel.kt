package com.example.padelmarcheofficial.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.Prenotazione
import com.example.padelmarcheofficial.dataclass.PrenotazioneAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrenotazioniAdminViewModel(private val gestioneFirebase: GestioneFirebase) : ViewModel() {

    private val _prenotazioniAmministratore = MutableLiveData<List<PrenotazioneAdmin>>()
    val prenotazioniAmministratore: LiveData<List<PrenotazioneAdmin>> get() = _prenotazioniAmministratore

    fun fetchPrenotazioniAmministratore(){
        viewModelScope.launch(Dispatchers.IO){
            val prenotazioni = gestioneFirebase.downloadPrenotazioniAmministratore()
            _prenotazioniAmministratore.postValue(prenotazioni)
        }
    }
}