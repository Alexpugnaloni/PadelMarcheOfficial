package com.example.padelmarcheofficial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.padelmarcheofficial.databinding.ActivityContattaciBinding

/**
 * Classe che effettua l'inflate di un activity con i contatti dei Developers
 */
class ContattaciActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContattaciBinding
    private lateinit var frecciaBack: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContattaciBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}