package com.example.padelmarcheofficial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.padelmarcheofficial.admin.AdminActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.padelmarcheofficial.databinding.ActivityLoginBinding
import com.example.padelmarcheofficial.dataclass.Account
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.superadmin.SuperadminActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date


/**
 * Classe che gestisce l'accesso all'applicazione tramite email e password
 * Permette inoltre tramite bottone "registrati" di registrarsi
 */
class AccediActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var firebaseAuth: FirebaseAuth
    var testing: Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val buttonclickregister = findViewById<Button>(R.id.button2)
        buttonclickregister.setOnClickListener {

            val intent = Intent(this, RegistratiActivity::class.java)
            startActivity(intent)
        }



        binding.button.setOnClickListener {

            val email = binding.editTextTextEmailAddress3.text.toString()
            val password = binding.editTextTextPassword.text.toString()


            /**
             * Qua si effettua un controllo della correttezza delle credenziali inserite e tramite un if,
             * se le credenziali sono corrette si va a verificare se chi ha effettuato l'accesso è un utente,
             * un amministratore o un superadmin dirigendolo nella sua activity principale
             */

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            if (user != null) {
                                val reftothis= this
                                CoroutineScope(Dispatchers.Main).launch {
                                    if (Account().isAdmin(user.email) != null) {
                                        val intent = Intent(reftothis, AdminActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else if (user.email == "superadmin@padelmarche.it"){
                                        val intent = Intent(reftothis, SuperadminActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else {
                                        setResult(1)
                                        finish()
                                        val replyIntent = Intent()
                                        replyIntent.putExtra("UserAccount", user)
                                        if (!testing) {
                                            setResult(Activity.RESULT_OK, replyIntent)
                                            finish()
                                        }
                                        val mainIntent = Intent(reftothis, MainActivity::class.java)
                                        startActivity(mainIntent)
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(this, "Accesso non riuscito: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Completa i campi", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
