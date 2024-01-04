package com.example.padelmarcheofficial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.padelmarcheofficial.databinding.ActivityNostriCentriBinding

class NostriCentriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNostriCentriBinding
    private lateinit var frecciaBack: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNostriCentriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            finish()

        }
    }
}