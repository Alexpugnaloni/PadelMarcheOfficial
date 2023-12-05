package com.example.padelmarcheofficial.ui.prenotazioni

import PrenotazioniAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.FragmentPrenotazioniBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.ui.home.HomeViewModel
import kotlinx.coroutines.launch


class PrenotazioniFragment : Fragment(),PrenotazioneClickListener {


    private var _binding: FragmentPrenotazioniBinding? = null

    private lateinit var viewModel: PrenotazioniViewModel
    private lateinit var viewModelFactory: PrenotazioniViewModelFactory
    private val gestioneFirebase = GestioneFirebase()

    private lateinit var recyclerView: RecyclerView
    private lateinit var prenotazioniAdapter: PrenotazioniAdapter

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_prenotazioni, container, false)


        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prenotazioniAdapter = PrenotazioniAdapter(this)
        recyclerView.adapter = prenotazioniAdapter

        viewModelFactory = PrenotazioniViewModelFactory(GestioneFirebase())
        viewModel = ViewModelProvider(this,viewModelFactory).get(PrenotazioniViewModel::class.java)
        viewModel.fetchPrenotazioniUtente()

        viewModel.prenotazioniUtente.observe(viewLifecycleOwner, Observer { prenotazioni ->
            prenotazioni?.let { prenotazioniAdapter.submitList(it) }
        })


    return view


    }
    override fun onPrenotazioneDelete(prenotazioneId: String) {
        // Esegui le azioni necessarie quando viene richiesta l'eliminazione della prenotazione
        // Ad esempio, chiamare il metodo per eliminare la prenotazione dalla classe GestioneFirebase
        lifecycleScope.launch {
            val gestioneFirebase = GestioneFirebase()
            gestioneFirebase.eliminaPrenotazione(prenotazioneId)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


