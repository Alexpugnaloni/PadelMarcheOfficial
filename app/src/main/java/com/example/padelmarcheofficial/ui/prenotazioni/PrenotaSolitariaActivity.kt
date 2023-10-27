package com.example.padelmarcheofficial.ui.prenotazioni

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.padelmarcheofficial.MainActivity
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityPrenotaSolitariaBinding
import com.example.padelmarcheofficial.databinding.ActivityPrenotaUnaPartita3Binding
import java.util.Calendar

class PrenotaSolitariaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrenotaSolitariaBinding


    private var myCalendar: Calendar = Calendar.getInstance()
    private lateinit var frecciaBack: ImageButton
    private lateinit var fasciaoraria1: Button
    private lateinit var fasciaoraria2: Button
    private lateinit var fasciaoraria3: Button
    private lateinit var fasciaoraria4: Button
    private lateinit var fasciaoraria5: Button
    private lateinit var fasciaoraria6: Button
    private lateinit var btnDatePicker: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrenotaSolitariaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

}
