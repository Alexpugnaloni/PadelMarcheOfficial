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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PrenotazioniAdminViewModel(private val gestioneFirebase: GestioneFirebase) : ViewModel() {

    private val _prenotazioniAmministratore = MutableLiveData<List<PrenotazioneAdmin>>()
    val prenotazioniAmministratore: LiveData<List<PrenotazioneAdmin>> get() = _prenotazioniAmministratore

    private var listaPrenotazioni: List<Prenotazione> = listOf()

    val formatoIntero = SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault())
    val formatoGiorno = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formatoOra = SimpleDateFormat("HH", Locale.getDefault())

    private val _fasciaSelezionata = MutableLiveData<Int>()
    val fasciaSelezionata: LiveData<Int> = _fasciaSelezionata

    var listafiltrata = listOf<Prenotazione>()
    var fasceOccupate: List<Int> = listOf()

    private val _dataSelezionata = MutableLiveData<Date>().apply {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        value = calendar.time
    }
    val dataSelezionata: LiveData<Date> = _dataSelezionata

    fun fetchPrenotazioniAmministratore(){
        viewModelScope.launch(Dispatchers.IO){
            val prenotazioni = gestioneFirebase.downloadPrenotazioniAmministratore()
            _prenotazioniAmministratore.postValue(prenotazioni)
        }
    }



    fun dataSelezionata(date: Date) {
        if (!date.equals(dataSelezionata.value)) {
            listafiltrata = listaPrenotazioni.filter {
                formatoGiorno.format(it.date) == formatoGiorno.format(date)
            }
            val listafiltrata2 = listafiltrata.filter {
                it.listautenti.size == 3
            }
            val orari = mutableListOf<Int>()
            for (prenotazione in listafiltrata2)
                orari.add(formatoOra.format(prenotazione.date).toInt())
            _fasciaSelezionata.postValue(0)
            fasceOccupate = orari
            _dataSelezionata.postValue(date)
        }
    }


}