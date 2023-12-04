package com.example.padelmarcheofficial.admin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.padelmarcheofficial.dataclass.CentroSportivo
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.Prenotazione
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AdminPrenotazioneViewModel : ViewModel() {
    val _terminato = MutableLiveData<Boolean>(false)
    val terminato: LiveData<Boolean> = _terminato

    val _alert = MutableLiveData<Boolean>(false)
    val alert: LiveData<Boolean> = _alert

    var count=0
    private var prenotazione: Prenotazione?=null

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
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        value = calendar.time
    }
    val dataSelezionata: LiveData<Date> = _dataSelezionata

    private val _fasciaSelezionata = MutableLiveData<Int>()
    val fasciaSelezionata: LiveData<Int> = _fasciaSelezionata

    var listafiltrata = listOf<Prenotazione>()

    /**
     * Funzione che istanzia le sedi effettuando download da Firebase
     */
    suspend fun init() {
        mappaSedi = GestioneFirebase().downloadSedi()
        _listaSedi.postValue(GestioneFirebase().downloadNomiSedi())
    }

    /**
     * Funzione che prende in input la sede selezionata dall'ArrayAdapter nella vista
     * ed effettua il download delle prenotazioni della sede di riferimento e le carica in
     * listaprenotazioni
     */

    fun sedeSelezionata(sede: String) {
        if (sede != _sedeSelezionata) {
            _sedeSelezionata = sede
            _fasciaSelezionata.postValue(0)
            CoroutineScope(Dispatchers.Main).launch {
                if (sede != "") {
                    val prenotazioniDeferred = CoroutineScope(Dispatchers.IO).async {
                        GestioneFirebase().downloadPrenotazioni(
                            mappaSedi[sede]!!.id,
                            Date.from(Instant.now())
                        )
                    }
                    listaPrenotazioni = prenotazioniDeferred.await()
                }
            }
        }
    }
    /**
     * Funzione che prende in input la data selezionata dal DatePicker nella vista
     *
     */
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

    /**
     * Funzione utlizzata nelle mappe dei bottoni per verificare se la fascia è disponibile o meno
     */
    fun fasciaSelezionata(int: Int) {
        if (int != _fasciaSelezionata.value)
            _fasciaSelezionata.postValue(int)
    }

    /**
     * Funzione che controlla ed effettua la prenotazione, caricandola in seguito su Firebase
     * Inoltre l'utente può effettuare prenotazioni multiple quando si trova in unisciti ad una partita,
     * anch'esse con i relativi controlli
     */
    fun conferma(context: Context, solitaria: Boolean) {
        if (_sedeSelezionata == "") {
            Toast.makeText(context, "Sede non selezionata", Toast.LENGTH_LONG).show()
        } else {
            if (false) {
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
                                GestioneFirebase().uploadPrenotazione(
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

    /**
     * Funzione che aggiorna una prenotazione già esistende andando ad aggiungere un ulteriore posto
     * ad una partita in una determinata fascia oraria ed in una determinata sede
     * Viene utilizzata nella classe unisciti ad una partita
     */

    fun updatePrenotazione(context: Context){
        CoroutineScope(Dispatchers.Main).launch {
            GestioneFirebase().updatePrenotazione(
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

    /**
     * Funzione che conta le prenotazioni in unisciti ad una partita
     */

    private fun contaPrenotazioni(prenotazione: Prenotazione): Int {
        val idutente = GestioneFirebase().getIdUtente()
        var count = 0
        if (prenotazione.utente.equals(idutente))
            count++
        for (id in prenotazione.listautenti)
            if (id.equals(idutente))
                count++
        return count
    }
}