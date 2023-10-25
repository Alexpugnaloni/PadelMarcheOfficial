package com.example.padelmarcheofficial

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.padelmarcheofficial.databinding.ActivityProfilo3Binding
import com.example.padelmarcheofficial.databinding.ActivityProfiloBinding
import com.example.padelmarcheofficial.dataclass.Funzionalita
import com.example.padelmarcheofficial.dataclass.UserValue
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfiloActivity3 : AppCompatActivity() {


    private lateinit var binding: ActivityProfiloBinding

    /**
     * variabile utilizzata per inserire il contorno alla foto del profilo
     */
    private lateinit var contornofoto: MaterialCardView

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
     * variabile utilizzata per la visualizzazione dell'immagine profilo
     */
    private lateinit var imgAccount: ImageView


    /**
     * variabili utilizzate per il salvataggio/modifica/ annullamento di ciò che viene fatto in questa classe
     */
    private lateinit var btnRimImg : Button
    private lateinit var btnSave: Button
    private lateinit var btnMod: Button
    private lateinit var btnBack : Button

    /**
     * variabile utilizzata per il settaggio di ciò che viene mostrato a video
     */
    private val accAppoggio : com.example.padelmarcheofficial.dataclass.Account by viewModels()

    /**
     * codice per la richiesta di inserimento di una foto da ProfileActivity
     */
    private val managingPHOTO=1234

    /**
     * variabile usata per salvare l'immagine inserita
     */
    private var imageBitMap: Bitmap?= null

    /**
     * variabili utilizzata per la gestione della visualizzazione dei bottoni
     */
    private var enabledmodifyng:Boolean=false
    private var check : Boolean = false






    /**
     * Binding del layout con le informazioni contenuta nell'account loggato
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilo3)
        val binding = DataBindingUtil.setContentView<ActivityProfilo3Binding>(this, R.layout.activity_profilo3)

//  setContentView(layout.activity_profilo)
        //  val binding = DataBindingUtil.setContentView<ActivityProfiloBinding>(this, layout.activity_profilo)  BELLAAAAAA

        //per gestire il mantenimento della possibiltà di modifica in caso di rotazione
        enabledmodifyng = if(savedInstanceState?.get("enable")!=null)
            (savedInstanceState.get("enable") as Boolean)
        else
            false

        initPoint(UserValue().getId(), UserValue().getEmail())

        //collego la variabile del layout con la variabile del ViewModel associtata
        binding.account = accAppoggio
        binding.lifecycleOwner = this


     //   descFoto = findViewById(R.id.myImageViewText)
        btnRimImg = findViewById(R.id.btnCancImg)
        btnMod = findViewById(R.id.btnModifica)
        btnSave = findViewById(R.id.btnSalvataggio)
        btnBack = findViewById(R.id.btnIndietro)

        contornofoto = findViewById<CardView>(R.id.externalcardview) as MaterialCardView

        nomeM = findViewById(R.id.CardNameModificabile)
        nomeNM = findViewById(R.id.nameNonModificabile)
        cognomeM = findViewById(R.id.CardSurnameModificabile)
        cognomeNM = findViewById(R.id.surnameNonModificabile)
        compleannoM = findViewById(R.id.CardEditTextDateModificabile)
        compleannoNM = findViewById(R.id.editTextDateNonModificabile)

//------NOME
        //se cambia il campo di testo associato al nome, cambio anche la proprietà dell'istanza di Account associata
        binding.CardNameModificabile.editText?.doOnTextChanged{ inputText, _, _, _ ->
            if(enabledmodifyng) {
                    accAppoggio.changeValue(1, inputText.toString())// Respond to input text change  COMMENTATA IO
                //updateSharedPref("nome", inputText.toString())
            }
        }
//------COGNOME
        binding.CardSurnameModificabile.editText?.doOnTextChanged { inputText, _, _, _ ->
                  accAppoggio.changeValue(2,inputText.toString())// Respond to input text change
            //updateSharedPref("cognome", inputText.toString())
        }
//------COMPLEANNO
        binding.CardEditTextDateModificabile.editText?.doOnTextChanged { inputText, _, _, _ ->
                accAppoggio.changeValue(5,inputText.toString())// Respond to input text change     COMMENTATA IO
            //updateSharedPref("data", inputText.toString())
        }
//------IMMAGINE
        imgAccount = findViewById(R.id.imgAccount)
        imgAccount.isClickable = false
        val imageObserver = Observer<Bitmap> { newValue ->
            binding.imgAccount.setImageBitmap(newValue)
            if (!check) {
                binding.imgAccount.isClickable = false
                check = true
            }
        }
        accAppoggio.imgbitmap.observe(this, imageObserver)
        imgAccount.setOnClickListener {
            val intent= Intent(
                this,
                ManagingPHOTO::class.java
            )
            intent.putExtra("Rounded",true)
            startActivityForResult(intent,managingPHOTO)
        }

        btnRimImg.isClickable = false
        btnRimImg.isEnabled = false
        btnRimImg.isVisible = false

        btnSave.isVisible=false
        btnBack.isVisible=false
        btnMod.isVisible=true

        //Gestione della comunicazione di eventuali errori nelle informazioni del profilo non modificabili
        /*    binding.repErr.setOnClickListener{
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:"+"MySocialUNIVPM@gmail.com")
                intent.putExtra(Intent.EXTRA_SUBJECT, "REPORT ERRORE")
                intent.putExtra(Intent.EXTRA_TEXT, "Ciao!\nSono ${accAppoggio._nome.value} ${accAppoggio._cognome.value}, matricola: ${accAppoggio._matricola.value}, e vorrei riportare: \n-")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            } */
        btnMod.setOnClickListener {
            if(Funzionalita().isOnline(it.context))
                enable()
        }
        //se annullo le modifiche fatte, riporto il tutto alla condizione iniziale,
        //andando a leggere di nuovo le informazioni da fb
        //e risettando nuovamente il binding
        binding.btnIndietro.setOnClickListener {
            disable()
            /*accAppoggio.check = false
            accAppoggio.rimossa = false
            accAppoggio.cambiata = false
            accAppoggio.originale = false*/
            //accAppoggio.loadInfo(id!!, em!!)
            initPoint(UserValue().getId(), UserValue().getEmail())
            binding.account = accAppoggio
        }
        binding.btnSalvataggio.setOnClickListener {
            if (Funzionalita().isOnline(baseContext.applicationContext as Context)&&verificaInserimento( binding.nameModificabile.text.toString(),binding.surnameModificabile.text.toString(),binding.editTextDateModificabile.text.toString()))
            {   accAppoggio.stampa()
                accAppoggio.update()
                disable()
            }
        }
        binding.btnCancImg.setOnClickListener {
            if(Funzionalita().isOnline(it.context)) {
                accAppoggio._imgbitmap.value = null
                imgAccount.setImageResource(R.drawable.ic_baseline_account_circle_24)
                //accAppoggio.presenzaImg = false
                /*
                accAppoggio.rimozioneImmagine()
                btnRimImg.isClickable = false*/
                btnRimImg.isEnabled = false
                btnRimImg.isVisible = false
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

        //  contornofoto.strokeColor= resources.getColor(color.DarkRed,theme) NON FUNZIONA IL COLORE DEL CONTORNO

        nomeM.isVisible=false
        nomeNM.isVisible=true

        cognomeM.isVisible=false
        cognomeNM.isVisible=true

        compleannoM.isVisible=false
        compleannoNM.isVisible=true

        imgAccount.isClickable = false


        btnRimImg.isEnabled = false
        btnRimImg.isVisible = false
        btnRimImg.isClickable = false

        btnSave.isEnabled = false
        btnSave.isVisible = false

        btnBack.isEnabled = false
        btnBack.isVisible = false

        btnMod.isEnabled = true
        btnMod.isClickable = true
        btnMod.isVisible = true
    }

    /**
     * Abilito tutte le funzionalità per la modifica
     */
    private fun enable(){
        if(accAppoggio._imgbitmap.value!= null){
            btnRimImg.isClickable = true
            btnRimImg.isEnabled = true
            btnRimImg.isVisible = true
        }
        /*
        if (!accAppoggio.originale){
            btnRimImg.isClickable = true
            btnRimImg.isEnabled = true
            btnRimImg.isVisible = true
        }*/
        else{
            btnRimImg.isEnabled = false
            btnRimImg.isVisible = false
            btnRimImg.isClickable = false
        }

        enabledmodifyng=true

        contornofoto.strokeColor= Color.GRAY

        nomeM.isVisible=true
        nomeNM.isVisible=false

        cognomeM.isVisible=true
        cognomeNM.isVisible=false

        compleannoM.isVisible=true
        compleannoNM.isVisible=false

        imgAccount.isClickable = true


        btnSave.isEnabled = true
        btnSave.isVisible = true

        btnBack.isEnabled = true
        btnBack.isVisible = true

        btnMod.isEnabled = false
        btnMod.isClickable = false
        btnMod.isVisible = false
    }

    /**
     * verifico che siano presenti tutti i dati necessari per l'inserimento di un nuovo account
     * @param nome il nome dell'utente
     * @param cognome il cognome dell'utente
     * @param compleanno la data di nascita dell'utente
     */
    private fun verificaInserimento(nome: String, cognome: String, compleanno: String): Boolean {
        if(nome.isBlank()) {
            Toast.makeText(baseContext, "Nome mancante", Toast.LENGTH_SHORT).show()
            return false
        }
        if(cognome.isBlank()) {
            Toast.makeText(baseContext, "Cognome mancante", Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            val date = LocalDate.parse(compleanno, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            if ((date.year < 1920) || (date.year > 2002)) {
                Toast.makeText(baseContext, "Data di nascita non corretta", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        } catch (e: Exception) {
            Toast.makeText(baseContext, "Data mancante o errata", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK && requestCode == managingPHOTO){
            //recuper0 dell'immagine salvata dalla classe managingPhoto da storage locale
            imageBitMap = BitmapFactory.decodeStream(openFileInput("myImage"))
            imgAccount.setImageBitmap(imageBitMap)
            //   accAppoggio.changeValue(0, null, imageBitMap)  COMMENTATA IO
            //accAppoggio.cambiata = true
            btnRimImg.isClickable = true
            btnRimImg.isEnabled = true
            btnRimImg.isVisible = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("enable",enabledmodifyng)
    }

    /**
     * Funzione per inizializzare i valori della variabile accouunt di appoggio, evitando di intaccare
     * i dati originali finchè l'utente non conferma. Se si è online vengono scaricati i dati da firebase,
     * altrimenti si caricano i dati salvati in locale.
     * @param id identificativo dell'utente
     * @param em l'email dell'utente
     */
    private fun initPoint(id:String, em:String){
        //se il dispositivo è online recupero le info da firebase
        if(Funzionalita().isOnline(cont = this)){
            accAppoggio.idD = id
            lifecycleScope.launch{
                accAppoggio.salva()
                accAppoggio.stampa()
                accAppoggio.seiVerificato = true
                accAppoggio._email.value = em
                if(!accAppoggio.presenzaImg) {
                    //accAppoggio._imgbitmap.value = null
                    imgAccount.setImageResource(R.drawable.ic_baseline_account_circle_24)
                }
            }
        }else{
            //se il dispositivo non è online recupero le informazioni salvate in locale a meno dell'immagine del profilo
            val temp = UserValue().getAccount()
            accAppoggio._nome.value= temp.nome.value
            accAppoggio._cognome.value= temp.cognome.value
            accAppoggio._email.value= temp.email.value
            accAppoggio._compleanno.value= temp.compleanno.value
            accAppoggio._cellulare.value= temp.cellulare.value
            accAppoggio._sesso.value= temp.sesso.value

        }
    }



}

