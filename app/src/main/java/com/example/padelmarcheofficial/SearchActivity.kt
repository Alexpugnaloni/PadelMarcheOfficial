package com.example.padelmarcheofficial.ui.search

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.dataclass.Funzionalita

/**
 * * Classe per gestire l'Activity della ricerca utenti
 *
 * @author Di Biase Alessandro, Donnini Valerio, Sopranzetti Lorenzo
 */


class SearchActivity : AppCompatActivity() { /*
    /**
     * Variabile SearchViewModel
     */
    private val viewModel: SearchViewModel by viewModels()

    private var Query:String? = null

    private lateinit var navcontroller:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Log.d("Entro", "Clicca sulla lente per cercare un utente")
        navcontroller = findNavController(R.id.nav_host)

        if (savedInstanceState != null){
            Query = savedInstanceState.getString("query")
        } else{
            Toast.makeText(this, "Clicca sulla lente per cercare un utente", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bar_search -> {
                val searchView: SearchView = item.actionView as SearchView
                searchView.isFocusable = true
                searchView.isIconified = false
                searchView.requestFocusFromTouch()
                searchView.queryHint = "Cerca Utente..."
                if (!Query.isNullOrBlank())
                    searchView.setQuery(Query, false)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    //Quando viene fatto il submit della ricerca viene aggiornato il valore del parametro ricerca del viewmodel
                    //che viene osservato dal fragmetn adibito alla ricerca.
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Query = query
                        val currentFragment = navcontroller.currentDestination?.label
                        if (currentFragment=="Ricerca") {
                            val action = UserFragmentDirections.actionUserToHome()
                            navcontroller.navigate(action)
                        }
                        if(Funzionalita().isOnline(this@SearchActivity))
                            viewModel.cercaUtenti(query)
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        Query = newText
                        return false
                    }
                })
                searchView.setOnCloseListener {
                    item.collapseActionView()
                    false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val appoggio = Query ?: ""
        outState.putString("query",appoggio)
    } */
}