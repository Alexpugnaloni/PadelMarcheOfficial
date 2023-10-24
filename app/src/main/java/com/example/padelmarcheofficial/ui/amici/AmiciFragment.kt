package com.example.padelmarcheofficial.ui.amici

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.databinding.FragmentAmiciBinding

class AmiciFragment : Fragment()/*,RecyclerAdapter.ViewHolder.ClickListener */{

    private var _binding: FragmentAmiciBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!





    /*

        /**
         * Questa funzione gestisce la ricerca: quando si fa click sul pulsante della ricerca si naviga al fragment che gestice la ricerca utilizzando
         * un viewmodel per gestire il passaggio della stringa fornita dagli utenti
         * @param item ciò che viene cliccato
         */
       override fun onOptionsItemSelected(item: MenuItem): Boolean {   /*DICE CHE NON VUOLE L'OVERRIDE*/

            when (item.itemId) {

                //si avvia l'intent per la ricerca degli utenti
                R.id.nav_amici -> {
                    val intentSearch = Intent(this, SearchActivity::class.java)
                    startActivity(intentSearch)
                }
                //si avvia l'intent per la visualizzazione e modifica del proprio pofilo
                R.id.nav_profilo -> {
                    val intent = Intent(this, ProfiloActivity::class.java)
                    //Log.d("PRIMA INTENT", "Verifica")
                    startActivity(intent)
                }

                android.R.id.home -> {
                    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            return super.onOptionsItemSelected(item)

        } */







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
        val amiciViewModel =
            ViewModelProvider(this).get(AmiciViewModel::class.java)

        _binding = FragmentAmiciBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        amiciViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}