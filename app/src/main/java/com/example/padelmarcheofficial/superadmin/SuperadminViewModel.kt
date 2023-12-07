package com.example.padelmarcheofficial.superadmin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SuperadminViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    private val gestioneFirebase = GestioneFirebase()

    private val _numPrenotazioniTotaliOggi = MutableLiveData<Int>()
    val numPrenotazioniTotaliOggi: LiveData<Int>
        get() = _numPrenotazioniTotaliOggi

    private val _numPrenotazioniTotaliSettimanaPassata = MutableLiveData<Int>()
    val numPrenotazioniTotaliSettimanaPassata: LiveData<Int>
        get() = _numPrenotazioniTotaliSettimanaPassata

    private val _numPrenotazioniTotaliMesePassato = MutableLiveData<Int>()
    val numPrenotazioniTotaliMesePassato: LiveData<Int>
        get() = _numPrenotazioniTotaliMesePassato

    private val _numUtentiIscritti = MutableLiveData<Int>()
    val numUtentiIscritti: LiveData<Int>
        get() = _numUtentiIscritti

    private val _numPrenotazioniCentroSportivoOggi = MutableLiveData<Int>()
    val numPrenotazioniCentroSportivoOggi: LiveData<Int>
        get() = _numPrenotazioniCentroSportivoOggi

    private val _numPrenotazioniCentroSportivoSettimanaPassata = MutableLiveData<Int>()
    val numPrenotazioniCentroSportivoSettimanaPassata: LiveData<Int>
        get() = _numPrenotazioniCentroSportivoSettimanaPassata

    private val _numPrenotazioniCentroSportivoMesePassato = MutableLiveData<Int>()
    val numPrenotazioniCentroSportivoMesePassato: LiveData<Int>
        get() = _numPrenotazioniCentroSportivoMesePassato

    fun checkSuperadminisLoggato() : Boolean{
        if(auth.currentUser != null)
            return true
        return false
    }

    fun logOut() {
        auth.signOut()
    }

    fun caricaNumPrenotazioniTotaliOggi() {
        viewModelScope.launch {
            val numPrenotazioni = gestioneFirebase.contaPrenotazioniTotaliOggi()
            _numPrenotazioniTotaliOggi.postValue(numPrenotazioni)
        }
    }
    fun caricaNumPrenotazioniTotaliSettimanaPassata() {
        viewModelScope.launch {
            val numPrenotazioni = gestioneFirebase.contaPrenotazioniTotaliSettimanaPassata()
            _numPrenotazioniTotaliSettimanaPassata.postValue(numPrenotazioni)
        }
    }

    fun caricaNumPrenotazioniTotaliMesePassato() {
        viewModelScope.launch {
            val numPrenotazioni = gestioneFirebase.contaPrenotazioniTotaliMesePassato()
            _numPrenotazioniTotaliMesePassato.postValue(numPrenotazioni)
        }
    }

    fun caricaNumUtentiIscritti() {
        viewModelScope.launch {
            val numIscritti = gestioneFirebase.contaUtentiStatistiche()
            _numUtentiIscritti.postValue(numIscritti)
        }
    }

    fun caricaNumPrenotazioniOggiPerCentroSportivo(centroSportivoId: String) {
        viewModelScope.launch {
            val numPrenotazioni = gestioneFirebase.contaPrenotazioniOggi(centroSportivoId)
            _numPrenotazioniCentroSportivoOggi.postValue(numPrenotazioni)
        }
    }

    fun caricaNumPrenotazioniSettimanaPassataPerCentroSportivo(centroSportivoId: String) {
        viewModelScope.launch {
            val numPrenotazioni = gestioneFirebase.contaPrenotazioniSettimanaPassata(centroSportivoId)
            _numPrenotazioniCentroSportivoSettimanaPassata.postValue(numPrenotazioni)
        }
    }

    fun caricaNumPrenotazioniMesePassatoPerCentroSportivo(centroSportivoId: String) {
        viewModelScope.launch {
            val numPrenotazioni = gestioneFirebase.contaPrenotazioniMesePassato(centroSportivoId)
            _numPrenotazioniCentroSportivoMesePassato.postValue(numPrenotazioni)
        }
    }



    /**
     * Funzione che istanzia le prenotazioni effettuando download da Firebase
     */
    suspend fun init() {

    }

}
