package com.example.padelmarcheofficial.ui.prenotazioni

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.MainActivity
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityPrenotaUnaPartita3Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Date


data class UtenteRegistrato(
    val nome: String,
    val cognome: String,
    val email: String,
    val numeroTelefono: String
)

data class CentroSportivo(val id: String, val nome: String, val indirizzo: String, val civico: String)


data class Prenotazione(
    val utente: String,//UtenteRegistrato,
    val centroSportivo: String,//CentroSportivo,
    val date: Date
)


class PrenotaUnaPartita3Activity : AppCompatActivity(), LifecycleOwner {

    private lateinit var binding: ActivityPrenotaUnaPartita3Binding
    private var myCalendar: Calendar = Calendar.getInstance()
    private lateinit var frecciaBack: ImageButton
    private lateinit var listesedi: List<String>
    private var mappabottoni: HashMap<Int, Button> = hashMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel = ViewModelProvider(this).get(PrenotaUnaPartitaViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.init()
        }

        val lifecycleowner = this
        binding = ActivityPrenotaUnaPartita3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        mappabottoni.put(9, binding.fasciaoraria1)
        mappabottoni.put(10, binding.fasciaoraria2)
        mappabottoni.put(11, binding.fasciaoraria3)
        mappabottoni.put(15, binding.fasciaoraria4)
        mappabottoni.put(16, binding.fasciaoraria5)
        mappabottoni.put(17, binding.fasciaoraria6)


        val adapter = ArrayAdapter(
            baseContext,
            android.R.layout.simple_spinner_item,
            viewmodel.listasedi.value!!
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProvincia.adapter = adapter

        viewmodel.listasedi.observe(lifecycleowner) {
            val adapter = ArrayAdapter(
                baseContext,
                android.R.layout.simple_spinner_item,
                viewmodel.listasedi.value!!
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerProvincia.adapter = adapter
            Log.d("ArrayAdapter", viewmodel.listasedi.value.toString())


        }

        binding.spinnerProvincia.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = viewmodel.listasedi.value!![position]
                    viewmodel.sedeSelezionata(selectedItem)
                }


                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        // Inizializza i tuoi bottoni e altri elementi visivi dall'XML qui...



        // Includi gli altri bottoni e elementi visivi.

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,{ datePicker: DatePicker, i: Int, i1: Int, i2: Int -> } , year, month, day)

        dpd.show()
        dpd.setOnDateSetListener(){ view, year, monthOfYear, dayOfMonth ->
            val dataString = "${dayOfMonth}-${monthOfYear +1}-$year"
            val formatoData = SimpleDateFormat("dd-MM-YYYY")

            try {
                val data: Date = formatoData.parse(dataString)
                viewmodel.dataSelezionata(data)
            } catch (e: Exception) {
                Log.d("data","Errore durante il parsing della data: ${e.message}")
            }
        }
        /*
       binding.btnDatePicker.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val dataString = "${dayOfMonth}-${monthOfYear +1}-$year"
                    val formatoData = SimpleDateFormat("dd-MM-YYYY")

                    try {
                        val data: Date = formatoData.parse(dataString)
                       viewmodel.dataSelezionata(data)
                    } catch (e: Exception) {
                        Log.d("data","Errore durante il parsing della data: ${e.message}")
                    }
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    // Chiamata per aggiornare i bottoni delle fasce orarie
                    updateFasciaOrariaButtons()
                },
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
            Log.d("prenotazioni", viewmodel.listaPrenotazioni.value.toString())
        }*/

        viewmodel.orariOccupati.observe(lifecycleowner) {
            toggleBottone(mappabottoni[9]!!, !it.contains(9))
            toggleBottone(mappabottoni[10]!!, !it.contains(10))
            toggleBottone(mappabottoni[11]!!, !it.contains(11))
            toggleBottone(mappabottoni[15]!!, !it.contains(15))
            toggleBottone(mappabottoni[16]!!, !it.contains(16))
            toggleBottone(mappabottoni[17]!!, !it.contains(17))

        }


    }

    fun toggleBottone(button: Button, abilita: Boolean) {
        if (abilita) {
            button.isEnabled = true
            button.setBackgroundResource(R.color.blu)
        } else {
            button.isEnabled = false
            button.setBackgroundResource(R.color.gray)
        }
    }

    fun updateFasciaOrariaButtons() { /*
        val db = FirebaseFirestore.getInstance()
        val prenotazioniRef = db.collection("prenotazioni")

        val fasciaOrarieButtons = listOf(fasciaoraria1, fasciaoraria2, fasciaoraria3, fasciaoraria4, fasciaoraria5, fasciaoraria6)

        val selectedDate = myCalendar.time
        val centroSportivoNome = centroSportivo.nome

        prenotazioniRef
            .whereEqualTo("centroSportivo.nome", centroSportivoNome)
            .whereGreaterThanOrEqualTo("fasciaOraria.fine", selectedDate)
            .whereLessThanOrEqualTo("fasciaOraria.inizio", selectedDate)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val prenotate = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Prenotazione::class.java)
                }

                for (button in fasciaOrarieButtons) {
                    val orarioButton = button as Button
                    val fasciaOrariaTesto = orarioButton.text.toString()

                    // Confronta il testo del pulsante con le fasce orarie prenotate.
                    val isPrenotata = prenotate.any { prenotazione ->
                        fasciaOrariaTesto.contains(prenotazione.fasciaOraria.inizio.toString()) &&
                                fasciaOrariaTesto.contains(prenotazione.fasciaOraria.fine.toString())
                    }

                    orarioButton.isEnabled = !isPrenotata

                    if (isPrenotata) {
                        orarioButton.setTextColor(Color.GRAY)
                    } else {
                        orarioButton.setTextColor(Color.BLACK)
                    }
                }
            }
            .addOnFailureListener { e ->
                println("Errore durante il recupero delle prenotazioni: $e")
            }

    }

    // Metodo immaginario per ottenere le fasce orarie disponibili per una data specifica.
    fun getAvailableFasciaOrarieForDate(date: Date): List<FasciaOraria> {
        // Qui dovresti implementare la logica per ottenere le fasce orarie disponibili
        // in base alle prenotazioni esistenti.
        // Restituisci una lista di fasce orarie disponibili per la data specifica.
        return emptyList() // Sostituisci con la tua logica effettiva.
    }

    fun prenota() {
        // ... Altri dati dell'utente, centro sportivo e fascia oraria ...

        verificaDisponibilitaFasciaOraria(centroSportivo, fasciaOraria)
    }

    private fun verificaDisponibilitaFasciaOraria(centroSportivo: CentroSportivo, fasciaOraria: FasciaOraria) {
        val db = FirebaseFirestore.getInstance()
        val prenotazioniRef = db.collection("prenotazioni")

        val query = prenotazioniRef
            .whereEqualTo("centroSportivo.nome", centroSportivo.nome)
            .whereGreaterThan("fasciaOraria.fine", fasciaOraria.inizio)
            .whereLessThan("fasciaOraria.inizio", fasciaOraria.fine)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    // La fascia oraria è disponibile, puoi procedere con la prenotazione.

                    // Creazione di un nuovo documento nel Firestore con i dati della prenotazione
                    val prenotazione = hashMapOf(
                        "utente" to hashMapOf(
                            "nome" to utente.nome,
                            "cognome" to utente.cognome,
                            "email" to utente.email,
                            "numeroTelefono" to utente.numeroTelefono
                        ),
                        "centroSportivo" to hashMapOf(
                            "nome" to centroSportivo.nome,
                            "indirizzo" to centroSportivo.indirizzo
                        ),
                        "fasciaOraria" to hashMapOf(
                            "inizio" to fasciaOraria.inizio,
                            "fine" to fasciaOraria.fine
                        )
                    )

                    db.collection("prenotazioni")
                        .add(prenotazione)
                        .addOnSuccessListener { documentReference ->
                            println("Prenotazione confermata con ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            println("Errore durante la prenotazione: $e")
                        }
                } else {
                    println("Fascia oraria non disponibile per la prenotazione nel centro sportivo selezionato.")
                }
            }
            .addOnFailureListener { e ->
                println("Errore durante la verifica di disponibilità: $e")
            } */
    }


}