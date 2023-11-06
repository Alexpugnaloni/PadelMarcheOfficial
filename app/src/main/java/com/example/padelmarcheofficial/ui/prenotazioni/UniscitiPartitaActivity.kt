package com.example.padelmarcheofficial.ui.prenotazioni

import android.app.AlertDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.MainActivity
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityUniscitiPartitaBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


/**
 * Classe che gestisce la prenotazione di una partita pubblica scegliendo la sede, il giorno e la fascia oraria
 */
class UniscitiPartitaActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var binding: ActivityUniscitiPartitaBinding
    private var myCalendar: Calendar = Calendar.getInstance()
    private lateinit var frecciaBack: ImageButton
    private lateinit var listesedi: List<String>
    private var mappabottoni: HashMap<Int, Button> = hashMapOf()
    private var mappatextview: HashMap<Int, TextView> = hashMapOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel = ViewModelProvider(this).get(PrenotaUnaPartitaViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.init()
        }

        val lifecycleowner = this
        binding = ActivityUniscitiPartitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mappabottoni.put(9, binding.fasciaoraria1)
        mappabottoni.put(10, binding.fasciaoraria2)
        mappabottoni.put(11, binding.fasciaoraria3)
        mappabottoni.put(15, binding.fasciaoraria4)
        mappabottoni.put(16, binding.fasciaoraria5)
        mappabottoni.put(17, binding.fasciaoraria6)

        //a differenza della PrenotaUnaPartita abbiamo anche le persone già presenti
        //nella partita per ogni bottone

        mappatextview.put(9, binding.personepresenti1)
        mappatextview.put(10, binding.personepresenti2)
        mappatextview.put(11, binding.personepresenti3)
        mappatextview.put(15, binding.personepresenti4)
        mappatextview.put(16, binding.personepresenti5)
        mappatextview.put(17, binding.personepresenti6)


        val adapter = ArrayAdapter(
            baseContext,
            android.R.layout.simple_spinner_item,
            viewmodel.listaSedi.value!!
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProvincia.adapter = adapter

        viewmodel.listaSedi.observe(lifecycleowner) {
            val adapter = ArrayAdapter(
                baseContext,
                android.R.layout.simple_spinner_item,
                viewmodel.listaSedi.value!!
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerProvincia.adapter = adapter
            Log.d("ArrayAdapter", viewmodel.listaSedi.value.toString())
        }

        binding.spinnerProvincia.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem = viewmodel.listaSedi.value!![position]
                    viewmodel.sedeSelezionata(selectedItem)
                }


                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this,
                { datePicker: DatePicker, i: Int, i1: Int, i2: Int -> },
                year,
                month,
                day)

            val dataOdierna =  Date()
            val formatoData = SimpleDateFormat("dd/MM/yyyy")

            dpd.show()
            dpd.setOnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val dataString = "${dayOfMonth}/${monthOfYear + 1}/$year"

                val dataInserita = formatoData.parse(dataString)


                if (dataInserita.before(dataOdierna)) {
                    Toast.makeText(this, "Non è possibile selezionare una data passata", Toast.LENGTH_LONG).show()
                } else {

                    try {
                        val data: Date = viewmodel.formatoGiorno.parse(dataString)
                        viewmodel.dataSelezionata(data)
                    } catch (e: Exception) {
                        Log.d("data", "Errore durante il parsing della data: ${e.message}")
                    }
                }
            }
        }

        viewmodel.dataSelezionata.observe(lifecycleowner) {
            binding.giornoscelto.text = "Giorno Scelto: " + viewmodel.formatoGiorno.format(it)
            val utenti = hashMapOf(9 to 0,10 to 0,11 to 0,15 to 0, 16 to 0,17 to 0)
            for(p in viewmodel.listafiltrata){
                utenti[viewmodel.formatoOra.format(p.date).toInt()]=p.listautenti.size+1
            }
            for(fascia in listOf(9,10,11,15,16,17))
                toggleview(mappabottoni[fascia]!!,mappatextview[fascia]!!,utenti[fascia]!!,fascia==viewmodel.fasciaSelezionata.value)
        }

        mappabottoni[9]!!.setOnClickListener {
            viewmodel.fasciaSelezionata(9)
        }
        mappabottoni[10]!!.setOnClickListener {
            viewmodel.fasciaSelezionata(10)
        }
        mappabottoni[11]!!.setOnClickListener {
            viewmodel.fasciaSelezionata(11)
        }
        mappabottoni[15]!!.setOnClickListener {
            viewmodel.fasciaSelezionata(15)
        }
        mappabottoni[16]!!.setOnClickListener {
            viewmodel.fasciaSelezionata(16)
        }
        mappabottoni[17]!!.setOnClickListener {
            viewmodel.fasciaSelezionata(17)
        }
        //associa ad ogni coppia fascia oraria - utenti un valore iniziale che è zero
        //che verrà incrementato in seguito quando gli utenti si aggiungeranno alla partita

        viewmodel.fasciaSelezionata.observe(lifecycleowner) {
            val utenti = hashMapOf(9 to 0,10 to 0,11 to 0,15 to 0, 16 to 0,17 to 0)
            for(p in viewmodel.listafiltrata){
                utenti[viewmodel.formatoOra.format(p.date).toInt()]=p.listautenti.size+1
            }
            for(fascia in listOf(9,10,11,15,16,17))
                toggleview(mappabottoni[fascia]!!,mappatextview[fascia]!!,utenti[fascia]!!,fascia==viewmodel.fasciaSelezionata.value)
        }

        viewmodel.terminato.observe(lifecycleowner){
            if (it == true){

                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("Prenotazione Confermata")
                    .setMessage("La tua prenotazione è stata confermata con successo.")
                    .setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setCancelable(false)

                val dialog = alertDialog.create()
                dialog.show()
            }

        }
        binding.btnConferma.setOnClickListener{
            viewmodel.conferma(lifecycleowner,true)
        }
    }
    /**
     * Funzione che abilita e disabilita le fasce orarie a seconda delle persone presenti nella prenotazione
     */
    fun toggleview(button: Button, textview: TextView, utenti :Int,selezionata: Boolean){
        val red = resources.getColor(R.color.LightRed,theme)
        val gray = resources.getColor(R.color.lightGray,theme)
        val blu = resources.getColor(R.color.blu,theme)
        val black = resources.getColor(R.color.black,theme)
        when(utenti){
            0 -> {
                button.isEnabled=true
                button.setBackgroundColor(if(selezionata)red else blu)
                textview.text = "0/4"
                textview.setTextColor(black)
            }
            1,2,3 -> {
                button.isEnabled=true
                button.setBackgroundColor(if(selezionata)red else blu)
                textview.text = "$utenti/4"
                textview.setTextColor(black)
            }
            4 -> {
                button.isEnabled=false
                button.setBackgroundColor(gray)
                textview.text = "4/4"
                textview.setTextColor(gray)
            }
            else -> {
                Toast.makeText(baseContext,"Errore nel download del numero di utenti prenotati",Toast.LENGTH_LONG).show()
            }
        }


    }
}
