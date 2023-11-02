package com.example.padelmarcheofficial.ui.prenotazioni

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.databinding.FragmentPrenotazioniBinding
import com.google.firebase.firestore.FirebaseFirestore

class PrenotazioniFragment : Fragment() {

   // private val firestore = FirebaseFirestore.getInstance()

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
/*
fun getPrenotazioni(callback: (List<String>) -> Unit, errorCallback: (Exception) -> Unit) {
    val prenotazioniRef = firestore.collection("Prenotazioni")
    prenotazioniRef.get()
        .addOnSuccessListener { documents ->
            val prenotazioni = mutableListOf<String>()
            for (document in documents) {
                val prenotazione = document.getString("descrizione")
                prenotazione?.let {
                    prenotazioni.add(it)
                }
            }
            callback(prenotazioni)
        }
        .addOnFailureListener { exception ->
            Log.e("PrenotazioniRepository", "Errore nel recupero delle prenotazioni", exception)
            errorCallback(exception)
        }
}
*/

