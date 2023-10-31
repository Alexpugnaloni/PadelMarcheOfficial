package com.example.padelmarcheofficial.ui.prenotazioni

import android.content.Context
import android.util.Log
import android.widget.Toast
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

    private val _listaSedi = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val listaSedi: LiveData<List<String>> = _listaSedi

    private val _listaPrenotazioni = MutableLiveData<List<Prenotazione>>().apply {
        value =
            listOf()//Prenotazione(UtenteRegistrato("Alex", "Pugnaloni", "prova@gmail.com", "3387826780"), CentroSportivo("Ancona", "via prova") )) // Inizializza con una lista che contiene un'istanza vuota di Prenotazione
    }

    val listaPrenotazioni: LiveData<List<Prenotazione>> = _listaPrenotazioni

    private var mappaSedi: HashMap<String, CentroSportivo> = hashMapOf()

    val formatoIntero = SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault())
    val formatoGiorno = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formatoOra = SimpleDateFormat("HH", Locale.getDefault())

    private val _fasceOccupate = MutableLiveData<List<Int>>().apply {
        value = listOf()

    }
    val fasceOccupate: LiveData<List<Int>> = _fasceOccupate

    private var _sedeSelezionata = ""

    private val _dataSelezionata = MutableLiveData<Date>().apply {
        value = Date()
    }
    val dataSelezionata: LiveData<Date> = _dataSelezionata

    private val _fasciaSelezionata = MutableLiveData<Int>()
    val fasciaSelezionata: LiveData<Int> = _fasciaSelezionata

    suspend fun init() {
        mappaSedi = GestioneAccount().downloadSedi()
        _listaSedi.postValue(GestioneAccount().downloadNomiSedi())
    }

    fun sedeSelezionata(sede: String) {
        if (sede != _sedeSelezionata){
            _sedeSelezionata = sede
            _fasciaSelezionata.postValue(0)
            CoroutineScope(Dispatchers.Main).launch {
                if (sede != "") {
                    val prenotazioniDeferred = CoroutineScope(Dispatchers.IO).async {
                        GestioneAccount().downloadPrenotazioni(mappaSedi[sede]!!.id, "27/10/2023")
                    }
                    val prenotazioni = prenotazioniDeferred.await()
                    _listaPrenotazioni.postValue(prenotazioni)
                }
            }
        }
    }

    fun dataSelezionata(date: Date) {
        if (!date.equals(dataSelezionata.value)){
            _fasciaSelezionata.postValue(0)
            _dataSelezionata.postValue(date)
            val listafiltrata = listaPrenotazioni.value?.filter {
                formatoGiorno.format(it.date) == formatoGiorno.format(date)
            }
            val orari = mutableListOf<Int>()
            if (listafiltrata != null) {
                for (prenotazione in listafiltrata)
                    orari.add(formatoOra.format(prenotazione.date).toInt())
                _fasceOccupate.postValue(orari)
            }
        }
    }

    fun fasciaSelezionata(int: Int){
        if(int != _fasciaSelezionata.value)
        _fasciaSelezionata.postValue(int)
    }

    fun conferma(context: Context) {
      if (_sedeSelezionata == ""){
          Toast.makeText(context,"Sede non selezionata",Toast.LENGTH_LONG).show()
      }else{
          if(false/*_dataSelezionata*/){
              Toast.makeText(context,"Sede non selezionata",Toast.LENGTH_LONG).show()
          }else{
              if(_fasciaSelezionata.value==0)
                  Toast.makeText(context,"Fascia non selezionata",Toast.LENGTH_LONG).show()
              else{
                 try{
                     var dataString = formatoGiorno.format(dataSelezionata.value)
                     dataString = "${fasciaSelezionata.value}:00 $dataString"
                     val data: Date = formatoIntero.parse(dataString)
                     GestioneAccount().uploadPrenotazione(mappaSedi[_sedeSelezionata]!!.id,data)
              }
              catch (e: Exception)
              {
                  Log.d("data", "Errore durante il parsing della data: ${e.message}")
              }

              }

          }

      }
    }
}