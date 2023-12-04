package com.example.padelmarcheofficial.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController

import com.example.padelmarcheofficial.AccediActivity
import com.example.padelmarcheofficial.MainActivity
import com.example.padelmarcheofficial.MainActivityViewModel

import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivityAdminBinding
import com.example.padelmarcheofficial.dataclass.Account
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.UserValue

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

    private var currentUser: FirebaseUser? = null
    private var auth: FirebaseAuth = Firebase.auth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel = ViewModelProvider(this).get(AdminViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.init()
        }
            binding = ActivityAdminBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navAdminViewDrawer: NavigationView = binding.navAdminviewdrawer

            val adminDrawerLayout: DrawerLayout = findViewById(R.id.admindrawer_layout)

            val headerView: View = navAdminViewDrawer.getHeaderView(0)

            val navEmailAdmin: TextView = headerView.findViewById(R.id.emailadmin)



            val toolbar : Toolbar = findViewById(R.id.toolbaradmin)
            setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,adminDrawerLayout,toolbar, R.string.cognome, R.string.nome)
        adminDrawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

       if (!viewmodel.checkAdminisLoggato())
            startActivity(Intent(this, AccediActivity::class.java))
        else {
            currentUser = auth.currentUser!!
            val reftothis = this
            CoroutineScope(Dispatchers.Main).launch {


                    navEmailAdmin.text = currentUser!!.email.toString()

                }
            }





            navAdminViewDrawer.setNavigationItemSelectedListener { it: MenuItem ->
                when (it.itemId) {
                    R.id.nav_adminlogout -> {
                        viewmodel.logOut()
                        startActivity(Intent(this, AccediActivity::class.java))

                        true
                    }
                    else -> {true}
                }
            }
        }
    }

