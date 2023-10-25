package com.example.padelmarcheofficial.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotaSolitariaActivity
import com.example.padelmarcheofficial.databinding.FragmentHomeBinding
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotaUnaPartita3Activity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

       /* val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        } */
        binding.prenotaunapartitaButton.setOnClickListener{
            val intent = Intent(this.context, PrenotaUnaPartita3Activity::class.java)
            startActivity(intent)
        }

        binding.prenotasolitariaButton.setOnClickListener {
            val intent = Intent(this.context, PrenotaSolitariaActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}