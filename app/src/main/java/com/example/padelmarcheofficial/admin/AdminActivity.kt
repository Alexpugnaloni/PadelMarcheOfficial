package com.example.padelmarcheofficial.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController

import com.example.padelmarcheofficial.AccediActivity

import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityAdminBinding
import com.example.padelmarcheofficial.dataclass.GestioneFirebase

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

            binding = ActivityAdminBinding.inflate(layoutInflater)
            setContentView(binding.root)


        }
    }
}
