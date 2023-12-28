package com.example.padelmarcheofficial.admin



import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 * Classe ViewModel che interagisce con l'adminactivity
 */
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


suspend fun init(){

}
}