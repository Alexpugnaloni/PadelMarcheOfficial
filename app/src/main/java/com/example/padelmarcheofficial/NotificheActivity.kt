package com.example.padelmarcheofficial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.padelmarcheofficial.databinding.ActivityNotificheBinding


class NotificheActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificheBinding
    private lateinit var btnBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificheBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }

    }
}