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
import android.widget.Toast
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
import java.util.Locale

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

        // Inizializza i tuoi bottoni e altri elementi visivi dall'XML qui...


        // Includi gli altri bottoni e elementi visivi.

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, 1)
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this,
                { datePicker: DatePicker, i: Int, i1: Int, i2: Int -> },
                year,
                month,
                day)

            val dataOdierna =  Date()//Calendar.getInstance()
         //   dataOdierna.timeInMillis
            val formatoData = SimpleDateFormat("dd/MM/yyyy")
    /////////////////////
            val cal = Calendar.getInstance()
            cal.time = dataOdierna // Imposta la data corrente

            cal.add(Calendar.DAY_OF_YEAR, +1) // Sottrae un giorno

            val dataDomani = cal.time
//////////////////


            dpd.show()
            dpd.setOnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
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
            for (fascia in listOf(9,10,11,15,16,17))
                toggleBottone(mappabottoni[fascia]!!, viewmodel.fasceOccupate.contains(fascia), viewmodel.fasciaSelezionata.value==fascia)
        }

        mappabottoni[9]!!.setOnClickListener{
            viewmodel.fasciaSelezionata(9)
        }
        mappabottoni[10]!!.setOnClickListener{
            viewmodel.fasciaSelezionata(10)
        }
        mappabottoni[11]!!.setOnClickListener{
            viewmodel.fasciaSelezionata(11)
        }
        mappabottoni[15]!!.setOnClickListener{
            viewmodel.fasciaSelezionata(15)
        }
        mappabottoni[16]!!.setOnClickListener{
            viewmodel.fasciaSelezionata(16)
        }
        mappabottoni[17]!!.setOnClickListener{
            viewmodel.fasciaSelezionata(17)
        }

        viewmodel.fasciaSelezionata.observe(lifecycleowner) {
            for (fascia in listOf(9,10,11,15,16,17))
                toggleBottone(mappabottoni[fascia]!!, viewmodel.fasceOccupate.contains(fascia), viewmodel.fasciaSelezionata.value==fascia)
        }

        binding.btnConferma.setOnClickListener{
            viewmodel.conferma(baseContext,false)
        }

        viewmodel.terminato.observe(lifecycleowner){
            if (it == true){
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Prenotazione Confermata")
                .setMessage("La tua prenotazione è stata confermata con successo.")
                .setPositiveButton("OK") { _, _ ->
                    // Quando l'utente fa clic su "OK", avvia l'Activity principale
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Chiudi questa Activity
                }
                .setCancelable(false) // Impedisce all'utente di chiudere l'AlertDialog toccando all'esterno

            val dialog = alertDialog.create()
            dialog.show()
        }

        }
    }

    fun toggleBottone(button: Button, disabilitata: Boolean, selezionata:Boolean) {
        button.isEnabled = !disabilitata
        val gray =resources.getColor(R.color.lightGray,theme)
        val blu =resources.getColor(R.color.blu,theme)
        val red = resources.getColor(R.color.LightRed,theme)
        button.setBackgroundColor(if (selezionata) red else if(!disabilitata) blu else gray)
    }



}
