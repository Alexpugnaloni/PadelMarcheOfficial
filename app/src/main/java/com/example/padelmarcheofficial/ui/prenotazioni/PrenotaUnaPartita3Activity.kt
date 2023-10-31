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


data class UtenteRegistrato(
    val nome: String,
    val cognome: String,
    val email: String,
    val numeroTelefono: String
)

data class CentroSportivo(
    val id: String,
    val nome: String,
    val indirizzo: String,
    val civico: String
)


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
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this,
                { datePicker: DatePicker, i: Int, i1: Int, i2: Int -> },
                year,
                month,
                day)

            dpd.show()
            dpd.setOnDateSetListener() { view, year, monthOfYear, dayOfMonth ->
                val dataString = "${dayOfMonth}/${monthOfYear + 1}/$year"

                try {
                    val data: Date = viewmodel.formatoGiorno.parse(dataString)
                    viewmodel.dataSelezionata(data)
                } catch (e: Exception) {
                    Log.d("data", "Errore durante il parsing della data: ${e.message}")
                }
            }
        }


        viewmodel.fasceOccupate.observe(lifecycleowner) {
            toggleBottone(mappabottoni[9]!!, !it.contains(9))
            toggleBottone(mappabottoni[10]!!, !it.contains(10))
            toggleBottone(mappabottoni[11]!!, !it.contains(11))
            toggleBottone(mappabottoni[15]!!, !it.contains(15))
            toggleBottone(mappabottoni[16]!!, !it.contains(16))
            toggleBottone(mappabottoni[17]!!, !it.contains(17))

        }


        viewmodel.dataSelezionata.observe(lifecycleowner) {
            binding.giornoscelto.text = "Giorno Scelto: " + viewmodel.formatoGiorno.format(it)
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
            val red =resources.getColor(R.color.LightRed)
            val blu =resources.getColor(R.color.blu)
            mappabottoni[9]!!.setBackgroundColor(if(it==9)red else blu)
            mappabottoni[10]!!.setBackgroundColor(if(it==10)red else blu)
            mappabottoni[11]!!.setBackgroundColor(if(it==11)red else blu)
            mappabottoni[15]!!.setBackgroundColor(if(it==15)red else blu)
            mappabottoni[16]!!.setBackgroundColor(if(it==16)red else blu)
            mappabottoni[17]!!.setBackgroundColor(if(it==17)red else blu)
        }

        binding.btnConferma.setOnClickListener{
            viewmodel.conferma(baseContext)
        }
    }

    fun toggleBottone(button: Button, abilita: Boolean) {
        button.isEnabled = abilita
        val gray =resources.getColor(R.color.lightGray)
        val blu =resources.getColor(R.color.blu)
        button.setBackgroundColor(if(abilita) blu else gray)
    }



}
