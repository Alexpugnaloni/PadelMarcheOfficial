package com.example.padelmarcheofficial.ui.prenotazioni

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.util.Date
import android.graphics.Color
import com.example.padelmarcheofficial.R
import android.widget.Button

data class UtenteRegistrato(val nome: String, val cognome: String, val email: String, val numeroTelefono: String)
data class CentroSportivo(val nome: String, val indirizzo: String)
data class FasciaOraria(val inizio: Date, val fine: Date)

class PrenotaUnaPartita : AppCompatActivity() {
    private var myCalendar: Calendar = Calendar.getInstance()

    // I tuoi bottoni e altri elementi visivi dell'XML qui...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prenota_una_partita3) // Assicurati di avere il layout corretto.

        // Inizializza i tuoi bottoni e altri elementi visivi dall'XML qui...

        val btnDatePicker = findViewById<Button>(R.id.btnDatePicker)
        val fasciaoraria1 = findViewById<Button>(R.id.fasciaoraria1)
        val fasciaoraria2 = findViewById<Button>(R.id.fasciaoraria2)
        // Includi gli altri bottoni e elementi visivi.

        btnDatePicker.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
        }

        // Includi gli altri listener per i bottoni delle fasce orarie e il pulsante di conferma.
    }

    fun updateFasciaOrariaButtons() {
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
            }
    }

}