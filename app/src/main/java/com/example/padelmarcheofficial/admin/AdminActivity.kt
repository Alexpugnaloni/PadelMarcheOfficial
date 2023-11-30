package com.example.padelmarcheofficial.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityAdminBinding
import com.example.padelmarcheofficial.databinding.ActivityPrenotaUnaPartitaBinding
import com.example.padelmarcheofficial.admin.AdminViewModel
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotaUnaPartitaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel = ViewModelProvider(this).get(AdminViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.init()

        }


        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}