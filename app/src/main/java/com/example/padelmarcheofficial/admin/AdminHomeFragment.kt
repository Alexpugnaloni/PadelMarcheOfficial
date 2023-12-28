package com.example.padelmarcheofficial.admin


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
import com.example.padelmarcheofficial.databinding.FragmentAdminHomeBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioneClickListener
import kotlinx.coroutines.launch
import java.util.Calendar


/**
 * Fragment dell'admin dove tramite delle recycleView vengono visualizzate le prenotazioni degli utenti
 */
class AdminHomeFragment : Fragment(), PrenotazioneClickListener {


    private var _binding: FragmentAdminHomeBinding? = null

    private lateinit var viewModel: PrenotazioniAdminViewModel
    private lateinit var viewModelFactory: PrenotazioniAdminViewModelFactory

    private lateinit var recyclerView: RecyclerView
    private lateinit var prenotazioniAdapter: PrenotazioniAdminAdapter

    private var myCalendar: Calendar = Calendar.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_home, container, false)


        recyclerView = view.findViewById(R.id.recyclerAdminView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        prenotazioniAdapter = PrenotazioniAdminAdapter(this)
        recyclerView.adapter = prenotazioniAdapter

        viewModelFactory = PrenotazioniAdminViewModelFactory(GestioneFirebase())
        viewModel = ViewModelProvider(this,viewModelFactory).get(PrenotazioniAdminViewModel::class.java)
        viewModel.fetchPrenotazioniAmministratore()

        viewModel.prenotazioniAmministratore.observe(viewLifecycleOwner, Observer { prenotazioni ->
            prenotazioni?.let { prenotazioniAdapter.submitList(it) }
             }
        )


        return view


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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}