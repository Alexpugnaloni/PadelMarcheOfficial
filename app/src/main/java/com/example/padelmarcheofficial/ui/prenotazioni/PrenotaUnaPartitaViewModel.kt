package com.example.padelmarcheofficial.ui.prenotazioni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.padelmarcheofficial.dataclass.GestioneAccount

class PrenotaUnaPartitaViewModel : ViewModel() {

    private val _listasedi = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val listasedi: LiveData<List<String>> = _listasedi

    private val _listaPrenotazioni = MutableLiveData<List<Prenotazione>>().apply {
        value = listOf(Prenotazione(UtenteRegistrato("Alex", "Pugnaloni", "prova@gmail.com", "3387826780"), CentroSportivo("Ancona", "via prova") )) // Inizializza con una lista che contiene un'istanza vuota di Prenotazione
    }

    val listaPrenotazioni: LiveData<List<Prenotazione>> = _listaPrenotazioni


    suspend fun init(){
        _listasedi.postValue(GestioneAccount().downloadNomiSedi())
        _listaPrenotazioni.postValue(GestioneAccount().downloadPrenotazioni("Ancona", "27/10/2023"))
    }
}