package com.example.padelmarcheofficial

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.padelmarcheofficial.databinding.ActivityPrenotaunapartitaBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PrenotaUnaPartitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrenotaunapartitaBinding

  //  val btnDatePicker = findViewById<Button>(R.id.btnDatePicker)
  //  var ScegliData = findViewById<TextView>(R.id.tvDate)

    val myCalendar = Calendar.getInstance()

    val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayofMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayofMonth)
        updateLable(myCalendar)
    }

    private fun updateLable(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrenotaunapartitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

    /*    btnDatePicker.setOnClickListener {
            DatePickerDialog(
                this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        } */

    }
}