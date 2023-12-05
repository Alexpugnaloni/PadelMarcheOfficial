package com.example.padelmarcheofficial.admin


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date


class AdminActivityViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    fun checkAdminisLoggato() : Boolean{
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