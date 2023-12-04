package com.example.padelmarcheofficial.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.FragmentAdminHomeBinding
import com.example.padelmarcheofficial.databinding.FragmentPrenotazioniBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioniViewModel
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioniViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [AdminHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null

    private lateinit var viewModel: PrenotazioniAdminViewModel
    private lateinit var viewModelFactory: PrenotazioniAdminViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var prenotazioniAdapter: PrenotazioniAdminAdapter

/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    } */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)


        recyclerView = view.findViewById(R.id.recyclerAdminView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prenotazioniAdapter = PrenotazioniAdminAdapter()
        recyclerView.adapter = prenotazioniAdapter

        viewModelFactory = PrenotazioniAdminViewModelFactory(GestioneFirebase())
        viewModel = ViewModelProvider(this,viewModelFactory).get(PrenotazioniAdminViewModel::class.java)
        viewModel.fetchPrenotazioniAmministratore()

        viewModel.prenotazioniAmministratore.observe(viewLifecycleOwner, Observer { prenotazioni ->
            prenotazioni?.let { prenotazioniAdapter.submitList(it) }
        })


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}