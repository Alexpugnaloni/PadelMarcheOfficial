package com.example.padelmarcheofficial.dataclass

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable


/**
 * * Classe per i dati e la gestione di un account.
 */
class Account : ViewModel(), Serializable {
    /**
     * Variabile per poter eseguire le operazioni su Firebase
     */
    private var gestioneFirebase: GestioneFirebase = GestioneFirebase()

    /**
     * La password dell'account dell'utente
     */
    private var _psw: MutableLiveData<String> = MutableLiveData("")

    /**
     * LiveData of **[_psw]**
     */
    var psw: LiveData<String>
        get() = _psw
        set(value) {
            _psw = value as MutableLiveData<String>
        }

    /**
     * L'email dell'account dell'utente
     */
    internal var _email: MutableLiveData<String> = MutableLiveData("")

    /**
     * LiveData of **[email]**
     */
    var email: LiveData<String>
        get() = _email
        set(value) {
            _email = value as MutableLiveData<String>
        }
    var idD: String = ""

    /**
     * Il nome dell'utente proprietario dell'account
     */
    internal var _nome: MutableLiveData<String> = MutableLiveData("")
    var nome: LiveData<String>
        get() = _nome
        set(value) {
            _nome = value as MutableLiveData<String>
            Log.d("Salvataggio", "nome: $value")
        }

    /**
     * Il cognome dell'utente proprietario dell'account
     */
    internal var _cognome: MutableLiveData<String> = MutableLiveData("")
    var cognome: LiveData<String>
        get() = _cognome
        set(value) {
            _cognome = value as MutableLiveData<String>
            Log.d("Salvataggio", "cognome: $value")
        }

    /**
     * La data di nascita dell'utente proprietario dell'account
     */
    internal var _compleanno: MutableLiveData<String> = MutableLiveData("")
    var compleanno: LiveData<String>
        get() = _compleanno
        set(value) {
            _compleanno = value as MutableLiveData<String>
            Log.d("Salvataggio", "compleanno: $value")
        }

    /**
     * Il numero di telefono dell'utente proprietario dell'account
     */
    internal var _cellulare: MutableLiveData<String> = MutableLiveData("")
    var cellulare: LiveData<String>
        get() = _cellulare
        set(value) {
            _cellulare = value as MutableLiveData<String>
            Log.d("Salvataggio", "cellulare: $value")
        }

    /**
     * Il genere dell'utente proprietario dell'account
     */
    internal var _sesso: MutableLiveData<String> = MutableLiveData("")
    var sesso: LiveData<String>
        get() = _sesso
        set(value) {
            _sesso = value as MutableLiveData<String>
            Log.d("Salvataggio", "sesso: $value")
        }

    suspend fun salva() {
        var dati: MutableMap<String, Any>
        withContext(Dispatchers.IO) {
            dati = gestioneFirebase.scaricaInformazioniAccount(account = this@Account)!!
        }
        val reftothis = this
        _nome.value = dati["nome"]?.toString()
        _cognome.value = dati["cognome"]?.toString()
        _compleanno.value = dati["dataDiNascita"]?.toString()
        _cellulare.value = dati["cellulare"]?.toString()
        _sesso.value = dati["sesso"]?.toString()
        gestioneFirebase.initUservalue()
    }


    /**
     * Funzione per controllo in fase di test: se richiamata stampa sulla console tutti i valori che
     * caratterizzano la variabile account
     */
    fun stampa() {

        Log.d("NOME", nome.value.toString())
        Log.d("COGNOME", cognome.value.toString())
        Log.d("EMAIL", email.value.toString())
        Log.d("PASSWORD", psw.value.toString())
        Log.d("COMPLEANNO", compleanno.value.toString())
        Log.d("CELLULARE", cellulare.toString())
        Log.d("SESSO", sesso.toString())
    }

    /**
     * Funzione unica per cambiare i valori delle variabili in account. Il parametro idVariabile
     * indica quale variabile si vuole modificare.
     * @param idVariabile identifica la variabile che si vuole modificare:
     * - 1 -> **[_nome]**
     * - 2 -> **[_cognome]**
     * - 3 -> **[_email]**
     * - 4 -> **[_psw]**
     * - 5 -> **[_compleanno]**
     * - 6 -> **[_cellulare]**
     * - 7 -> **[_sesso]**
     * @param value valore di tipo stringa necessario per tutti campi
     */

    fun changeValue(idVariabile: Int, value: String? = null) {

        when (idVariabile) {
            1 -> _nome.value = value.toString()
            2 -> _cognome.value = value.toString()
            3 -> _email.value = value.toString()
            4 -> _psw.value = value.toString()
            5 -> _compleanno.value = value.toString()
            6 -> _cellulare.value = value.toString()
            7 -> _sesso.value = value.toString()
        }
    }

    /**
     * Aggiorno l'account richiamando **[OperazioniSuFb.aggiornaAccount]**
     */
    fun update() {
        gestioneFirebase.aggiornaAccount(this)
    }
}