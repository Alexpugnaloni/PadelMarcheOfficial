package com.example.padelmarcheofficial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.padelmarcheofficial.databinding.ActivityWelcomeBinding


class AccediActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        print("fuori")
        val buttonclickregister = findViewById<Button>(R.id.button2)
        buttonclickregister.setOnClickListener {
            print("dajeee")
            val intent = Intent(this, RegistratiActivity::class.java)
            startActivity(intent)  }

     /*   binding.button2.setOnClickListener(){
            val intent = Intent(this, RegistratiActivity::class.java)
            startActivity(intent)
        }*/

        binding.button.setOnClickListener{

            val email =     binding.editTextTextEmailAddress3.text.toString()
            val password =  binding.editTextTextPassword.text.toString()


            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            } else Toast.makeText(this, "Completa i campi", Toast.LENGTH_SHORT).show()
        }
        }
}
