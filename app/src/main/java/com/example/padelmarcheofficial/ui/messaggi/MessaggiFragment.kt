package com.example.padelmarcheofficial.ui.messaggi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.databinding.FragmentMessaggiBinding
import com.example.padelmarcheofficial.R

class MessaggiFragment : Fragment()/*,RecyclerAdapter.ViewHolder.ClickListener */{

    private var _binding: FragmentMessaggiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


/*

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv: RecyclerView = view.findViewById(R.id.rv_utenti)
        rv.layoutManager = LinearLayoutManager(SearchActivity())
        viewModel.listapiena.observe(viewLifecycleOwner, { lista ->
            RecyclerAdapter(lista,this).notifyDataSetChanged()
            rv.adapter = RecyclerAdapter(lista,this)
            if(lista.size==0) {
                Toast.makeText(requireContext(), "Nessun utente trovato", Toast.LENGTH_LONG).show()
                Log.d("ListaVuota","Nessun utente trovato")
            }
        })
    }
    override fun onClickListener(id:String,email:String) {
        val action = SearchFragmentDirections.actionHomeToUser(id,email)
        findNavController().navigate(action)
    }

}*/






    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messaggiViewModel =
            ViewModelProvider(this).get(MessaggiViewModel::class.java)

        _binding = FragmentMessaggiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        messaggiViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}