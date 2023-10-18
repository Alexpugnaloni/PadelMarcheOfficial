package com.example.padelmarcheofficial.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.padelmarcheofficial.dataclass.OperazioniSuFb
import com.google.firebase.auth.FirebaseAuth
import com.example.padelmarcheofficial.dataclass.Account
import kotlinx.coroutines.launch

class HomeViewModel {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val utenteDB = OperazioniSuFb()

    private var _utente = MutableLiveData(Account())
    val utente: LiveData<Account>
        get() = _utente


   /* fun getUtente() {
        viewModelScope.launch {
            _utente.value = utenteDB.initUservalue(auth.currentUser!!.email!!)
        }

    } */

    fun logOut() {
        auth.signOut()
    }

}