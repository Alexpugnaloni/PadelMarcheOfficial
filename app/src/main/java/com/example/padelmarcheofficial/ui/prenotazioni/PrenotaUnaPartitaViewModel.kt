package com.example.padelmarcheofficial.ui.prenotazioni

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.padelmarcheofficial.dataclass.CentroSportivo
import com.example.padelmarcheofficial.dataclass.GestioneAccount
import com.example.padelmarcheofficial.dataclass.Prenotazione
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class PrenotaUnaPartitaViewModel : ViewModel() {

    val _terminato = MutableLiveData<Boolean>(false)
    val terminato: LiveData<Boolean> = _terminato

    val _alert = MutableLiveData<Boolean>(false)
    val alert: LiveData<Boolean> = _alert

    var count=0
    private var prenotazione:Prenotazione?=null

    private val _listaSedi = MutableLiveData<List<String>>().apply {
        value = listOf("")

    }
    val listaSedi: LiveData<List<String>> = _listaSedi

    private var listaPrenotazioni: List<Prenotazione> = listOf()

    private var mappaSedi: HashMap<String, CentroSportivo> = hashMapOf()

    val formatoIntero = SimpleDateFormat("hh:mm dd/MM/yyyy", Locale.getDefault())
    val formatoGiorno = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formatoOra = SimpleDateFormat("HH", Locale.getDefault())

    var fasceOccupate: List<Int> = listOf()

    private var _sedeSelezionata = ""

    private val _dataSelezionata = MutableLiveData<Date>().apply {
        value = Date()
    }
    val dataSelezionata: LiveData<Date> = _dataSelezionata

    private val _fasciaSelezionata = MutableLiveData<Int>()
    val fasciaSelezionata: LiveData<Int> = _fasciaSelezionata

    var listafiltrata = listOf<Prenotazione>()

    suspend fun init() {
        mappaSedi = GestioneAccount().downloadSedi()
        _listaSedi.postValue(GestioneAccount().downloadNomiSedi())
    }

    fun sedeSelezionata(sede: String) {
        if (sede != _sedeSelezionata) {
            _sedeSelezionata = sede
            _fasciaSelezionata.postValue(0)
            CoroutineScope(Dispatchers.Main).launch {
                if (sede != "") {
                    val prenotazioniDeferred = CoroutineScope(Dispatchers.IO).async {
                        GestioneAccount().downloadPrenotazioni(
                            mappaSedi[sede]!!.id,
                            Date.from(Instant.now())
                        )
                    }
                    listaPrenotazioni = prenotazioniDeferred.await()
                }
            }
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

    fun fasciaSelezionata(int: Int) {
        if (int != _fasciaSelezionata.value)
            _fasciaSelezionata.postValue(int)
    }

    fun conferma(context: Context, solitaria: Boolean) {
        if (_sedeSelezionata == "") {
            Toast.makeText(context, "Sede non selezionata", Toast.LENGTH_LONG).show()
        } else {
            if (false/*_dataSelezionata*/) {
                Toast.makeText(context, "Sede non selezionata", Toast.LENGTH_LONG).show()
            } else {
                if (_fasciaSelezionata.value == 0)
                    Toast.makeText(context, "Fascia non selezionata", Toast.LENGTH_LONG).show()
                else {
                    try {
                        var dataString = formatoGiorno.format(dataSelezionata.value!!)
                        dataString = "${fasciaSelezionata.value}:00 $dataString"
                        val data: Date = formatoIntero.parse(dataString)!!
                        for (p in listafiltrata)
                            if (p.date.equals(data))
                                prenotazione = p
                        if (prenotazione == null)
                            CoroutineScope(Dispatchers.Main).launch {
                                GestioneAccount().uploadPrenotazione(
                                    mappaSedi[_sedeSelezionata]!!.id,
                                    data,
                                    solitaria
                                )
                                _terminato.postValue(true)
                            }
                        else
                            if (contaPrenotazioni(prenotazione!!) > 0) {
                                count = contaPrenotazioni(prenotazione!!)
                                val builder = AlertDialog.Builder(context)
                                builder.setTitle("Conferma")
                                builder.setMessage("Per questa sede, giorno e fascia oraria hai già prenotato $count post${if (count == 1) "o" else "i"}. Sei sicuro di voler prenotare un'altro posto?")
                                builder.setPositiveButton("Sì") { dialog: DialogInterface, which: Int ->
                                    updatePrenotazione(context)
                                }
                                builder.setNegativeButton("No") { dialog: DialogInterface, which: Int -> }

                                val dialog = builder.create()
                                dialog.show()

                                Log.d("afagga", "jfbaiuciauchau")
                            } else {
                                updatePrenotazione(context)
                            }
                    } catch (e: Exception) {
                        Log.d("data", "Errore durante il parsing della data: ${e.message}")
                    }
                }
            }
        }
    }

    fun updatePrenotazione(context: Context){
        CoroutineScope(Dispatchers.Main).launch {
            GestioneAccount().updatePrenotazione(
                mappaSedi[_sedeSelezionata]!!.id,
                prenotazione!!,
                context
            )
            _terminato.postValue(true)
        }
        Toast.makeText(
            context,
            "Prenotazione del ${count + 1} posto inviata",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun contaPrenotazioni(prenotazione: Prenotazione): Int {
        val idutente = GestioneAccount().getIdUtente()
        var count = 0
        if (prenotazione.utente.equals(idutente))
            count++
        for (id in prenotazione.listautenti)
            if (id.equals(idutente))
                count++
        return count
    }
}