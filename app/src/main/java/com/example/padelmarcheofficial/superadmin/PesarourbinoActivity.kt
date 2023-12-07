package com.example.padelmarcheofficial.superadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityAnconaBinding
import com.example.padelmarcheofficial.databinding.ActivityPesarourbinoBinding

class PesarourbinoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesarourbinoBinding
    private lateinit var frecciaBack: ImageButton
    private lateinit var superadminViewModel: SuperadminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesarourbinoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frecciaBack = findViewById<ImageButton>(R.id.frecciatoolbar)
        frecciaBack.setOnClickListener {
            val intent = Intent(this, SuperadminActivity::class.java)
            intent.putExtra("centroSportivoId", "VapW6qTS1HOHEmORefB5")
            startActivity(intent)
        }

        superadminViewModel = ViewModelProvider(this).get(SuperadminViewModel::class.java)


        val centroSportivoId = intent.getStringExtra("centroSportivoId") ?: "VapW6qTS1HOHEmORefB5"
        superadminViewModel.caricaNumPrenotazioniOggiPerCentroSportivo(centroSportivoId)
        superadminViewModel.caricaNumPrenotazioniSettimanaPassataPerCentroSportivo(centroSportivoId)
        superadminViewModel.caricaNumPrenotazioniMesePassatoPerCentroSportivo(centroSportivoId)


        superadminViewModel.numPrenotazioniCentroSportivoOggi.observe(this, Observer { numPrenotazioni ->
            val totaleInEuro = numPrenotazioni * 60
            binding.textentrategiornaliereTotView.text = "Entrate Giornaliere Totali: $totaleInEuro €"
        })

        superadminViewModel.numPrenotazioniCentroSportivoSettimanaPassata.observe(this, Observer { numPrenotazioni ->
            val totaleInEuro = numPrenotazioni * 60
            binding.textentrateSettimanaliTotView.text = "Entrate Settimanali Totali: $totaleInEuro €"
        })


        superadminViewModel.numPrenotazioniCentroSportivoMesePassato.observe(this, Observer { numPrenotazioni ->
            val totaleInEuro = numPrenotazioni * 60
            binding.textentrateMensiliTotView.text = "Entrate Mensili Totali: $totaleInEuro €"
        })

    }
}