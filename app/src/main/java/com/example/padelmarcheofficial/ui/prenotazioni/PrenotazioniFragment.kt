package com.example.padelmarcheofficial.ui.prenotazioni

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.databinding.FragmentPrenotazioniBinding


class PrenotazioniFragment : Fragment() {


    private var _binding: FragmentPrenotazioniBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val prenotazioniViewModel =
            ViewModelProvider(this).get(PrenotazioniViewModel::class.java)

        _binding = FragmentPrenotazioniBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


