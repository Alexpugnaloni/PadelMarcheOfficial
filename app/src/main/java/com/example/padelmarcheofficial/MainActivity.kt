package com.example.padelmarcheofficial

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.padelmarcheofficial.admin.AdminActivity
import com.example.padelmarcheofficial.databinding.ActivityMainBinding
import com.example.padelmarcheofficial.dataclass.Account
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.UserValue
import com.example.padelmarcheofficial.superadmin.SuperadminActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Classe principale e che si occupa di gestire la navigazione all'interno dell'applicazione
 */
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
    private var database: GestioneFirebase = GestioneFirebase()

    private var auth: FirebaseAuth = Firebase.auth



    /**
     * Funzione alla creazione dell'interfaccia grafica del MainActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



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


        val headerView : View = navViewDrawer.getHeaderView(0)
        val navNomeUtente : TextView = headerView.findViewById(R.id.nomeutente)
        val navEmailUtente : TextView =headerView.findViewById(R.id.emailutente)

        /**
         * effettua un controllo se l'utente è già loggato, se non è loggato rimanda al login
         */

        if (!model.checkUtenteisLoggato())
            startActivity(Intent(this, AccediActivity::class.java))
            else {
            currentUser = auth.currentUser!!
            val reftothis = this
            CoroutineScope(Dispatchers.Main).launch {
                if (Account().isAdmin(currentUser!!.email) != null) {
                    startActivity(Intent(reftothis, AdminActivity::class.java))
                }else if (currentUser!!.email == "superadmin@padelmarche.it")
                    startActivity(Intent(reftothis, SuperadminActivity::class.java))
                else {
                    navNomeUtente.text = UserValue().getNome() + " " + UserValue().getCognome()
                    navEmailUtente.text = currentUser!!.email.toString()
                    initPoint()
                }
            }
        }


        navViewDrawer.setNavigationItemSelectedListener {it:MenuItem ->
            when(it.itemId){
                R.id.nav_logout -> {
                    model.logOut()
                    startActivity(Intent(this,AccediActivity::class.java))

                    true
                }
                R.id.nav_profilo -> {
                    startActivity(Intent(this,ProfiloActivity::class.java))
                    true

                }

                R.id.nav_notifiche -> {
                    startActivity(Intent(this, NotificheActivity::class.java))
                    true

                }

                R.id.nav_nostricentri -> {
                    startActivity(Intent(this, NostriCentriActivity::class.java))
                    true

                }

                R.id.nav_contattaci -> {
                    startActivity(Intent(this, ContattaciActivity::class.java))
                    true

                }

                else -> {true}


            }
        }


    }

    /**
     * Funzione che recupera le informazioni dell'account loggato
     */
    private fun initPoint() {
        acc.idD = currentUser!!.uid
        acc._email.value = currentUser!!.email

        lifecycleScope.launch {
            acc.salva()
            database.initUservalue()

        }
    }
}