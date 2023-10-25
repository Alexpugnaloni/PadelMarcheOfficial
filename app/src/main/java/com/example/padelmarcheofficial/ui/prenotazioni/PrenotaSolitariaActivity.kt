package com.example.padelmarcheofficial.ui.prenotazioni

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityPrenotaSolitariaBinding

class PrenotaSolitariaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrenotaSolitariaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrenotaSolitariaBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}
