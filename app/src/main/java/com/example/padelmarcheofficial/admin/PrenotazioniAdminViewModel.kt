package com.example.padelmarcheofficial.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.PrenotazioneAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date


/**
 * Classe ViewModel che interagisce con GestioneFirebase per il caricamento delle prenotazioni dell'admin
 */
class PrenotazioniAdminViewModel(private val gestioneFirebase: GestioneFirebase) : ViewModel() {

    private val _prenotazioniAmministratore = MutableLiveData<List<PrenotazioneAdmin>>()
    val prenotazioniAmministratore: LiveData<List<PrenotazioneAdmin>> get() = _prenotazioniAmministratore


    /**
     * Metodo che carica le prenotazioni prese da un metodo di GestioneFirebase
     */
    fun fetchPrenotazioniAmministratore(){
        viewModelScope.launch(Dispatchers.IO){
            val prenotazioni = gestioneFirebase.downloadPrenotazioniAmministratore()
            _prenotazioniAmministratore.postValue(prenotazioni)
        }
    }

    fun downloadprenotazioni(data: Date) {

        viewModelScope.launch(Dispatchers.IO){
            val prenotazioni = gestioneFirebase.downloadPrenotazioniAmministratore()
            _prenotazioniAmministratore.postValue(prenotazioni)
        }
    }

}