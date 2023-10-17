package com.example.padelmarcheofficial.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeViewModel {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val utenteDB = UtenteDB()

    private var _utente = MutableLiveData(Utente())
    val utente: LiveData<Utente>
        get() = _utente


    fun getUtente() {
        viewModelScope.launch {
            _utente.value = utenteDB.getUtente(auth.currentUser!!.email!!)
        }

    }

    fun logOut() {
        auth.signOut()
    }

}