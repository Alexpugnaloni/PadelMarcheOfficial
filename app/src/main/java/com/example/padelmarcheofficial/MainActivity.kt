package com.example.padelmarcheofficial

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.padelmarcheofficial.databinding.ActivityMainBinding
import com.example.padelmarcheofficial.dataclass.OperazioniSuFb
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var model =MainActivityViewModel()

    /**
     * Variabile utilizzata per salvare le informazioni della persona che si logga
     */
    private val acc: com.example.padelmarcheofficial.dataclass.Account by viewModels()

    /**
     * Variabile utilizzata per il controllo in memoria della presenza in memoria di un utente
     */
    private var currentUser: FirebaseUser? = null


    /**
     * Variabile utilizzata per le operazioni fatte su +Firebase*
     */
    private var database: OperazioniSuFb = OperazioniSuFb()

    private var auth: FirebaseAuth = Firebase.auth

    /**
     * Funzione alla creazione dell'interfaccia grafica del MainActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!model.checkUtenteisLoggato()){
            startActivity(Intent(this, AccediActivity::class.java))
        }
        else   {
            currentUser=auth.currentUser
            initPoint()
        }

        val navViewBottom: BottomNavigationView = findViewById(R.id.nav_viewbottom)

        val navViewDrawer : NavigationView = binding.navViewdrawer

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)
        navViewBottom.setupWithNavController(navController)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.cognome, R.string.nome)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        /**
         *
         */

        navViewDrawer.setNavigationItemSelectedListener {it:MenuItem ->
            when(it.itemId){
                R.id.nav_logout -> {
                    model.logOut()
                    startActivity(Intent(this,AccediActivity::class.java))

                    true
                }
                R.id.nav_profilo -> {
                    startActivity(Intent(this,ProfiloActivity3::class.java))
                    true

                }
                else -> {true}
            }
        }


    }


    private fun initPoint() {
        acc.idD = currentUser!!.uid
        acc._email.value = currentUser!!.email

        lifecycleScope.launch {
            acc.salva()
            database.initUservalue()
            //}
        }
    }
}