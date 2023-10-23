package com.example.padelmarcheofficial

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.padelmarcheofficial.databinding.ActivityMainBinding
import com.example.padelmarcheofficial.databinding.AppBarMainBinding
import com.example.padelmarcheofficial.databinding.ContentMainBinding
import com.example.padelmarcheofficial.ui.search.SearchActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingBottom: ContentMainBinding
    private lateinit var bindingIntermedio : AppBarMainBinding

    private var model =MainActivityViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!model.checkUtenteisLoggato()){
            startActivity(Intent(this, AccediActivity::class.java))
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

      //  supportActionBar?.setDisplayHomeAsUpEnabled(true)




/*

        /**
         * Questa funzione gestisce la ricerca: quando si fa click sul pulsante della ricerca si naviga al fragment che gestice la ricerca utilizzando
         * un viewmodel per gestire il passaggio della stringa fornita dagli utenti
         * @param item ciò che viene cliccato
         */
       override fun onOptionsItemSelected(item: MenuItem): Boolean {   /*DICE CHE NON VUOLE L'OVERRIDE*/

            when (item.itemId) {

                //si avvia l'intent per la ricerca degli utenti
                R.id.nav_amici -> {
                    val intentSearch = Intent(this, SearchActivity::class.java)
                    startActivity(intentSearch)
                }
                //si avvia l'intent per la visualizzazione e modifica del proprio pofilo
                R.id.nav_profilo -> {
                    val intent = Intent(this, ProfiloActivity::class.java)
                    //Log.d("PRIMA INTENT", "Verifica")
                    startActivity(intent)
                }

                android.R.id.home -> {
                    val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            return super.onOptionsItemSelected(item)

        } */

    }
}