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





            val emailInput = "ancona@padelmarche.it"
            val passwordInput = "Anconaancona"


            fun isAdmin(email: String?): Boolean {
                // Verifica se l'utente corrente corrisponde a un utente specifico
                return email == "ancona@padelmarche.it"
            }

            firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null && isAdmin(user.email)) {
                        // Reindirizza alla vista specifica per l'amministratore (AdminActivity)
                        val intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                        finish() // Chiudi l'activity di login
                    } else {
                        // L'utente non è un amministratore
                        // Puoi mostrare un messaggio di errore o reindirizzare altrove
                    }
                } else {
                    // Accesso non riuscito
                    Toast.makeText(this, "Accesso non riuscito: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }



            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        setResult(1)
                        finish()
                        val replyintent=Intent()
                        replyintent.putExtra("UserAccount",user)
                        if(!testing) {
                            setResult(Activity.RESULT_OK, replyintent)
                            finish()
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            } else Toast.makeText(this, "Completa i campi", Toast.LENGTH_SHORT).show()
        }
    }
}
