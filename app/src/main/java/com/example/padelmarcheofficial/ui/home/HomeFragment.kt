package com.example.padelmarcheofficial.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.ui.prenotazioni.UniscitiPartitaActivity
import com.example.padelmarcheofficial.databinding.FragmentHomeBinding
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotaUnaPartitaActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.prenotaunapartitaButton.setOnClickListener {
            val intent = Intent(this.context, PrenotaUnaPartitaActivity::class.java)
            startActivity(intent)
        }

        binding.prenotasolitariaButton.setOnClickListener {
            val intent = Intent(this.context, UniscitiPartitaActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}