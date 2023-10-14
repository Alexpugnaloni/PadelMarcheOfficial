package com.example.padelmarcheofficial

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }
        binding.button.setOnClickListener{
            val name = binding.name.text.toString()
            val surname = binding.surname.text.toString()
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            val birth = binding.editTextDate.text.toString()
            val phone = binding.phone.text.toString()
            val sesso = binding.spinnerSesso.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEMpty()){
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