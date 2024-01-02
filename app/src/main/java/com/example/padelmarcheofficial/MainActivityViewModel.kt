package com.example.padelmarcheofficial

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivityViewModel : ViewModel(){
    private var auth: FirebaseAuth = Firebase.auth

    fun logOut() {
        auth.signOut()
    }
}