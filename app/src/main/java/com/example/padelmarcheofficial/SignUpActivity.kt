package com.example.padelmarcheofficial

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.padelmarcheofficial.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }
        binding.button.setOnClickListener{
            val name = binding.name.text.toString()
            val surname = binding.surname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val compleanno = binding.editTextDate.text.toString()
            val cellulare = binding.phone.text.toString()
            val sesso = binding.spinnerSesso.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, SignInActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            } else Toast.makeText(this, "Completa i campi", Toast.LENGTH_SHORT).show()
        }
    }
}