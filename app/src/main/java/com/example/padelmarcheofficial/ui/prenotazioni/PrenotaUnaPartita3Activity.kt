package com.example.padelmarcheofficial.ui.prenotazioni

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityPrenotaUnaPartita3Binding


class PrenotaUnaPartita3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPrenotaUnaPartita3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrenotaUnaPartita3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ArrayAdapter.createFromResource(this, R.array.provincia_array,android.R.layout.simple_spinner_item).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            binding.spinnerProvincia.adapter=arrayAdapter
        }

    }


    /*
    private lateinit var binding: ActivityPrenotaunapartitaBinding

    val btnDatePicker = findViewById<Button>(R.id.btnDatePicker)
    var ScegliData = findViewById<TextView>(R.id.tvDate)

    val myCalendar = Calendar.getInstance()

    val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
        updateLable(myCalendar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrenotaunapartitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnDatePicker.setOnClickListener {
            DatePickerDialog(
                this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

    }


    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
    }*/


}