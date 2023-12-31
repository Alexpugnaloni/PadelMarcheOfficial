package com.example.padelmarcheofficial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.padelmarcheofficial.admin.AdminActivity
import com.example.padelmarcheofficial.dataclass.Account
import com.example.padelmarcheofficial.superadmin.SuperadminActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BenvenutoActivity : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null

    private val durata_splash_screen = 2000

    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_benvenuto)

        Handler().postDelayed(
            {
                prossimaActivity()
            }, durata_splash_screen.toLong()
        )
    }

        /**
         * effettua un controllo se l'utente è già loggato, se non è loggato rimanda al login
         */
    private fun prossimaActivity(){
        if (!checkUtenteisLoggato())
            startActivity(Intent(this, AccediActivity::class.java))
        else {
            currentUser = auth.currentUser!!
            val reftothis = this
            CoroutineScope(Dispatchers.Main).launch {
                if (Account().isAdmin(currentUser!!.email) != null) {
                    startActivity(Intent(reftothis, AdminActivity::class.java))
                }else if (currentUser!!.email == "superadmin@padelmarche.it")
                    startActivity(Intent(reftothis, SuperadminActivity::class.java))
                else {
                        startActivity(Intent(reftothis,UserActivity::class.java))
                }
            }
        }
    }

    fun checkUtenteisLoggato() : Boolean{
        if(auth.currentUser != null)
            return true
        return false
    }
}