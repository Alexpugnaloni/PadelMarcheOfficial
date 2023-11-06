package com.example.padelmarcheofficial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.padelmarcheofficial.databinding.ActivityProfiloBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.UserValue
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfiloActivity : AppCompatActivity() {

    /**
     * variabili utilizzate per la gestione del nome dell'utente
     */
    private lateinit var nomeM: View
    private lateinit var nomeNM: CardView

    /**
     * variabili utilizzate per la gestione del cognome dell'utente
     */
    private lateinit var cognomeM: View
    private lateinit var cognomeNM: CardView

    /**
     * variabili utilizzate per la gestione del compleanno dell'utente
     */
    private lateinit var compleannoM: View
    private lateinit var compleannoNM: CardView

    /**
     * variabili utilizzate per la gestione del cellulare dell'utente
     */
    private lateinit var cellulareM: View
    private lateinit var cellulareNM: CardView

    /**
     * variabili utilizzate per il salvataggio/modifica/ annullamento di ciò che viene fatto in questa classe
     */
    private lateinit var btnSave: Button
    private lateinit var btnMod: Button
    private lateinit var btnBack : Button
    private lateinit var btnHomeBack : Button

    /**
     * variabile utilizzata per il settaggio di ciò che viene mostrato a video
     */
    private val accAppoggio : com.example.padelmarcheofficial.dataclass.Account by viewModels()


    /**
     * variabile utilizzata per la gestione della visualizzazione dei bottoni
     */
    private var enabledmodifyng:Boolean=false

    /**
     * Binding del layout con le informazioni contenute nell'account loggato
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo)
        val binding = DataBindingUtil.setContentView<ActivityProfiloBinding>(this, R.layout.activity_profilo)


        initPoint(UserValue().getId(), UserValue().getEmail())

        //collego la variabile del layout con la variabile del ViewModel Account, ossia accappoggio

        binding.account = accAppoggio
        binding.lifecycleOwner = this


        btnMod = findViewById(R.id.btnModifica)
        btnHomeBack = findViewById(R.id.btnBackHome)
        btnSave = findViewById(R.id.btnSalvataggio)
        btnBack = findViewById(R.id.btnIndietro)


        nomeM = findViewById(R.id.CardNameModificabile)
        nomeNM = findViewById(R.id.nameNonModificabile)
        cognomeM = findViewById(R.id.CardSurnameModificabile)
        cognomeNM = findViewById(R.id.surnameNonModificabile)
        compleannoM = findViewById(R.id.CardEditTextDateModificabile)
        compleannoNM = findViewById(R.id.editTextDateNonModificabile)
        cellulareM = findViewById(R.id.CardCellulareModificabile)
        cellulareNM = findViewById(R.id.cellulareNonModificabile)


//------NOME
        //se cambia il campo di testo associato al nome, cambio anche la proprietà dell'istanza di Account associata
        binding.CardNameModificabile.editText?.doOnTextChanged{ inputText, _, _, _ ->
            if(enabledmodifyng) {
                accAppoggio.changeValue(1, inputText.toString())

            }
        }
//------COGNOME
        binding.CardSurnameModificabile.editText?.doOnTextChanged { inputText, _, _, _ ->
            accAppoggio.changeValue(2,inputText.toString())

        }
//------COMPLEANNO
        binding.CardEditTextDateModificabile.editText?.doOnTextChanged { inputText, _, _, _ ->
            accAppoggio.changeValue(5,inputText.toString())

        }

//------CELLULARE
        binding.CardCellulareModificabile.editText?.doOnTextChanged { inputText, _, _, _ ->
            accAppoggio.changeValue(6,inputText.toString())

        }


        btnSave.isVisible=false
        btnBack.isVisible=false
        btnMod.isVisible=true
        btnHomeBack.isVisible= true

        btnHomeBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        btnMod.setOnClickListener {
            if(GestioneFirebase().isOnline(it.context))
                enable()
        }
        //se annullo le modifiche fatte, riporto il tutto alla condizione iniziale,
        //andando a leggere di nuovo le informazioni da fb
        //e risettando nuovamente il binding
        binding.btnIndietro.setOnClickListener {
            disable()

            initPoint(UserValue().getId(), UserValue().getEmail())
            binding.account = accAppoggio
        }
        binding.btnSalvataggio.setOnClickListener {
            if (GestioneFirebase().isOnline(baseContext.applicationContext as Context)&&verificaInserimento( binding.nameModificabile.text.toString(),binding.surnameModificabile.text.toString(),binding.celluareModificabile.text.toString(),binding.editTextDateModificabile.text.toString()))
            {   accAppoggio.stampa()
                accAppoggio.update()
                disable()
            }
        }

        if(enabledmodifyng)
            enable()
        else
            disable()
    }

    /**
     * Disabilito tutte le funzionalità per la modifica
     */
    private fun disable(){

        enabledmodifyng=false


        nomeM.isVisible=false
        nomeNM.isVisible=true

        cognomeM.isVisible=false
        cognomeNM.isVisible=true

        compleannoM.isVisible=false
        compleannoNM.isVisible=true

        cellulareM.isVisible=false
        cellulareNM.isVisible=true


        btnSave.isEnabled = false
        btnSave.isVisible = false

        btnBack.isEnabled = false
        btnBack.isVisible = false

        btnMod.isEnabled = true
        btnMod.isClickable = true
        btnMod.isVisible = true

        btnHomeBack.isEnabled = true
        btnHomeBack.isClickable = true
        btnHomeBack.isVisible = true
    }

    /**
     * Abilito tutte le funzionalità per la modifica
     */
    private fun enable(){
        enabledmodifyng=true

        nomeM.isVisible=true
        nomeNM.isVisible=false

        cognomeM.isVisible=true
        cognomeNM.isVisible=false

        compleannoM.isVisible=true
        compleannoNM.isVisible=false

        cellulareM.isVisible=true
        cellulareNM.isVisible=false

        btnSave.isEnabled = true
        btnSave.isVisible = true

        btnBack.isEnabled = true
        btnBack.isVisible = true

        btnMod.isEnabled = false
        btnMod.isClickable = false
        btnMod.isVisible = false

        btnHomeBack.isEnabled = false
        btnHomeBack.isClickable = false
        btnHomeBack.isVisible = false
    }

    /**
     * verifico che siano presenti tutti i dati necessari per l'inserimento di un nuovo account
     */
    private fun verificaInserimento(nome: String, cognome: String, cellulare:String, compleanno: String): Boolean {
        if(nome.isBlank()) {
            Toast.makeText(baseContext, "Nome mancante", Toast.LENGTH_SHORT).show()
            return false
        }
        if(cognome.isBlank()) {
            Toast.makeText(baseContext, "Cognome mancante", Toast.LENGTH_SHORT).show()
            return false
        }
        if(cellulare.isBlank()) {
            Toast.makeText(baseContext, "Cellulare mancante", Toast.LENGTH_SHORT).show()
            return false
        }

            val date = LocalDate.parse(compleanno, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            if (date != null) {
                return true
            } else {
                Toast.makeText(baseContext, "Data di nascita non corretta", Toast.LENGTH_SHORT).show()
                return false
            }



    }


    /**
     * Funzione per inizializzare i valori della variabile account di appoggio, evitando di intaccare
     * i dati originali finchè l'utente non conferma. Se si è online vengono scaricati i dati da firebase,
     * altrimenti si caricano i dati salvati in locale.
     */
    private fun initPoint(id:String, em:String) {
        //se il dispositivo è online recupero le info da firebase
        if (GestioneFirebase().isOnline(cont = this)) {
            accAppoggio.idD = id
            lifecycleScope.launch {
                accAppoggio.salva()
                accAppoggio.stampa()
                accAppoggio._email.value = em
            }
                //se il dispositivo non è online recupero le informazioni salvate in locale a meno dell'immagine del profilo
                val temp = UserValue().getAccount()
                accAppoggio._nome.value = temp.nome.value
                accAppoggio._cognome.value = temp.cognome.value
                accAppoggio._email.value = temp.email.value
                accAppoggio._compleanno.value = temp.compleanno.value
                accAppoggio._cellulare.value = temp.cellulare.value
                accAppoggio._sesso.value = temp.sesso.value

            }
        }

    }




