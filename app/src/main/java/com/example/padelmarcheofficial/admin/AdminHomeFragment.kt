package com.example.padelmarcheofficial.admin


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityPrenotaUnaPartitaBinding
import com.example.padelmarcheofficial.databinding.FragmentAdminHomeBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioneClickListener
import com.google.type.DateTime
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Fragment dell'admin dove tramite delle recycleView vengono visualizzate le prenotazioni degli utenti
 */
class AdminHomeFragment : Fragment(), PrenotazioneClickListener {


    private lateinit var binding: FragmentAdminHomeBinding

    private lateinit var viewModel: PrenotazioniAdminViewModel
    private lateinit var viewModelFactory: PrenotazioniAdminViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var prenotazioniAdapter: PrenotazioniAdminAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        recyclerView = view.findViewById(R.id.recyclerAdminView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prenotazioniAdapter = PrenotazioniAdminAdapter(this)
        recyclerView.adapter = prenotazioniAdapter

        viewModelFactory = PrenotazioniAdminViewModelFactory(GestioneFirebase())
        viewModel = ViewModelProvider(this,viewModelFactory).get(PrenotazioniAdminViewModel::class.java)
        viewModel.fetchPrenotazioniAmministratore()

        viewModel.prenotazioniAmministratore.observe(viewLifecycleOwner, Observer { prenotazioni ->
            prenotazioni?.let { prenotazioniAdapter.submitList(it) }
        })

        binding.btnDatePicker2.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                viewModel.downloadprenotazioni(selectedDate)

            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }



    /**
     * Metodo che si occupa dell'eliminazione di una prenotazione da parte dell'admin
     * richiamando un metodo di GestioneFirebase
     */
    override fun onPrenotazioneDelete(prenotazioneId: String) {
        lifecycleScope.launch {
            val gestioneFirebase = GestioneFirebase()
            gestioneFirebase.eliminaPrenotazione(prenotazioneId)
        }
    }



}