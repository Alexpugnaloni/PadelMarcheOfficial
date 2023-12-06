package com.example.padelmarcheofficial.superadmin

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SuperadminActivityViewModel : ViewModel() {
    private var auth: FirebaseAuth = Firebase.auth

    fun checkSuperadminisLoggato() : Boolean{
        if(auth.currentUser != null)
            return true
        return false
    }

    fun logOut() {
        auth.signOut()
    }

    /**
     * Funzione che istanzia le prenotazioni effettuando download da Firebase
     */
    suspend fun init() {
//ci andrà la funzione che ricarica la lista di tutte le prenotazioni
    }

}
