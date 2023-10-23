package com.example.padelmarcheofficial.ui.messaggi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.padelmarcheofficial.dataclass.OperazioniSuFb


class MessaggiViewModel : ViewModel() {

/*

    private val database: OperazioniSuFb = OperazioniSuFb()
    private val _listapiena = MutableLiveData<MutableList<MutableMap<String, Any>>>()
    val listapiena: LiveData<MutableList<MutableMap<String, Any>>>
        get() = _listapiena
    /**
     * Ricerca gli utenti che contengono nel nome e cognome il testo ricercato dall'utente corrente.
     * Ricerca effettuata tramite **[OperazioniSuFb.ricercaUtenti]**
     */
    fun cercaUtenti(nome: String?){
        viewModelScope.launch(Dispatchers.IO) {
            if (nome != null) {
                withContext(Dispatchers.IO) {
                    val lista = database.ricercaUtenti(nome)
                    SetlistaNomi(lista)
                }
            }
        }
    }

    /**
     * Aggiorna la variabile elenco dei diversi utenti che soddisfano la ricerca
     */
    fun SetlistaNomi(listona: MutableList<MutableMap<String,Any>>){
        _listapiena.postValue(listona)
    }
}*/







    private val _text = MutableLiveData<String>().apply {
        value = "This is Friends Fragment"
    }
    val text: LiveData<String> = _text
}