package com.example.padelmarcheofficial.ui.prenotazioni

import android.widget.DatePicker
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.padelmarcheofficial.dataclass.GestioneAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrenotaUnaPartitaViewModel : ViewModel() {

    private val _listasedi = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val listasedi: LiveData<List<String>> = _listasedi

    private val _listaPrenotazioni = MutableLiveData<List<Prenotazione>>().apply {
        value = listOf()//Prenotazione(UtenteRegistrato("Alex", "Pugnaloni", "prova@gmail.com", "3387826780"), CentroSportivo("Ancona", "via prova") )) // Inizializza con una lista che contiene un'istanza vuota di Prenotazione
    }

    val listaPrenotazioni: LiveData<List<Prenotazione>> = _listaPrenotazioni

    private var sedi : HashMap<String,CentroSportivo> = hashMapOf()

    val formatoGiorno = SimpleDateFormat("dd:MM:YYYY", Locale.getDefault())
    val formatoOra = SimpleDateFormat("HH", Locale.getDefault())

    private val _orariOccupati = MutableLiveData<List<Int>>().apply {
        value = listOf()

    }
    val orariOccupati: LiveData<List<Int>> = _orariOccupati


    fun sedeSelezionata (sede: String) {
        CoroutineScope(Dispatchers.Main).launch {
        if (sede != ""){
            val prenotazioniDeferred = CoroutineScope(Dispatchers.IO).async {
                GestioneAccount().downloadPrenotazioni(sedi[sede]!!.id, "27/10/2023")
            }
            val prenotazioni = prenotazioniDeferred.await()
            _listaPrenotazioni.postValue(prenotazioni)
        }
        }

    }

    private val _dataPrenotazioni = MutableLiveData<Date>().apply {
        value = Date()
    }

    val dataPrenotazioni: LiveData<Date> = _dataPrenotazioni

    fun dataSelezionata (date: Date) {
        _dataPrenotazioni.postValue(date)
        var listafiltrata = listaPrenotazioni.value?.filter {
           formatoGiorno.format(it.date)== formatoGiorno.format(date)
        }
        var orari = mutableListOf<Int>()
        if (listafiltrata != null) {
            for( prenotazione in listafiltrata)
                orari.add(formatoOra.format(prenotazione.date).toInt())
            _orariOccupati.postValue(orari)
        }


        val oraComeStringa = formatoOra.format(date)
        val oraComeIntero = oraComeStringa.toInt()
    }

    suspend fun init(){
        sedi=GestioneAccount().downloadSedi()
        _listasedi.postValue(GestioneAccount().downloadNomiSedi())
    }
}