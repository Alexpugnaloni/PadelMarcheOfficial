package com.example.padelmarcheofficial.superadmin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.FragmentPrenotazioniBinding
import com.example.padelmarcheofficial.databinding.FragmentSuperadminHomeBinding


class SuperadminFragment : Fragment() {

    private lateinit var superadminViewModel: SuperadminViewModel
    private var _binding: FragmentSuperadminHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuperadminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        superadminViewModel = ViewModelProvider(this).get(SuperadminViewModel::class.java)


        superadminViewModel.caricaNumPrenotazioniTotaliOggi()
        superadminViewModel.caricaNumPrenotazioniTotaliSettimanaPassata()
        superadminViewModel.caricaNumPrenotazioniTotaliMesePassato()
        superadminViewModel.caricaNumUtentiIscritti()


        superadminViewModel.numPrenotazioniTotaliOggi.observe(viewLifecycleOwner, Observer { numPrenotazioni ->
            val totaleInEuro = numPrenotazioni * 60
            binding.textentrategiornaliereTotView.text = "Entrate Giornaliere Totali: $totaleInEuro €"
        })

        superadminViewModel.numPrenotazioniTotaliSettimanaPassata.observe(viewLifecycleOwner, Observer { numPrenotazioni ->
            val totaleInEuro = numPrenotazioni * 60
            binding.textentrateSettimanaliTotView.text = "Entrate Settimanali Totali: $totaleInEuro €"
        })


        superadminViewModel.numPrenotazioniTotaliMesePassato.observe(viewLifecycleOwner, Observer { numPrenotazioni ->
            val totaleInEuro = numPrenotazioni * 60
            binding.textentrateMensiliTotView.text = "Entrate Mensili Totali: $totaleInEuro €"
        })

        superadminViewModel.numUtentiIscritti.observe(viewLifecycleOwner, Observer { numUtenti ->
            binding.textnumiscrittiView.text = "Numero di iscritti: $numUtenti"
        })




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}