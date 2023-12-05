package com.example.padelmarcheofficial.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.FragmentAdminHomeBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Use the [AdminHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminHomeFragment : Fragment(),LifecycleOwner{

    private lateinit var binding: FragmentAdminHomeBinding

    private var _binding: FragmentAdminHomeBinding? = null

    private lateinit var viewModel: PrenotazioniAdminViewModel
    private lateinit var viewModelFactory: PrenotazioniAdminViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var prenotazioniAdapter: PrenotazioniAdminAdapter

    private var myCalendar: Calendar = Calendar.getInstance()

/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    } */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
             }
        )




        binding.btnDatePicker2.setOnClickListener {
            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR, 1)
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireActivity(),
                { datePicker: DatePicker, i: Int, i1: Int, i2: Int -> },
                year,
                month,
                day)

            val dataOdierna =  Date()
            val formatoData = SimpleDateFormat("dd/MM/yyyy")

            dpd.show()
            dpd.setOnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val dataString = "${dayOfMonth}/${monthOfYear + 1}/$year"

                val dataInserita = formatoData.parse(dataString)

                if (dataInserita.before(dataOdierna)) {
                    Toast.makeText(requireActivity(), "Non è possibile selezionare una data passata", Toast.LENGTH_LONG).show()
                } else {

                    try {
                        val data: Date = viewModel.formatoGiorno.parse(dataString)
                        viewModel.dataSelezionata(data)
                    } catch (e: Exception) {
                        Log.d("data", "Errore durante il parsing della data: ${e.message}")
                    }
                }
            }
        }

        return view


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}