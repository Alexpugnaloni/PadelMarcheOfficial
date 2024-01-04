package com.example.padelmarcheofficial.superadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.padelmarcheofficial.AccediActivity
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.databinding.ActivitySuperadminBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Classe che gestisce la navigazione del superadmin nella sua activity
 */
class SuperadminActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var binding: ActivitySuperadminBinding
    private var currentUser: FirebaseUser? = null
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewmodel = ViewModelProvider(this).get(SuperadminViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            viewmodel.init()
        }
        binding = ActivitySuperadminBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val navAdminViewDrawer: NavigationView = binding.navSuperadminviewdrawer

        val superadminDrawerLayout: DrawerLayout = findViewById(R.id.superadmindrawer_layout)

        val headerView: View = navAdminViewDrawer.getHeaderView(0)

        val navEmailSuperAdmin: TextView = headerView.findViewById(R.id.emailsuperadmin)



        val toolbar : Toolbar = findViewById(R.id.toolbarsuperadmin)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,superadminDrawerLayout,toolbar, R.string.cognome, R.string.nome)
        superadminDrawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        if (!viewmodel.checkSuperadminisLoggato())
            startActivity(Intent(this, AccediActivity::class.java))
        else {
            currentUser = auth.currentUser!!
            val reftothis = this
            CoroutineScope(Dispatchers.Main).launch {


                navEmailSuperAdmin.text = currentUser!!.email.toString()
            }
        }



        navAdminViewDrawer.setNavigationItemSelectedListener { it: MenuItem ->
            when (it.itemId) {

                R.id.nav_centroPesaroUrbino -> {
                    val intent = Intent (this, CentroActivity::class.java)
                    intent.putExtra("centroSportivoId", "VapW6qTS1HOHEmORefB5")
                    intent.putExtra("nomecentro", "Pesaro Urbino")
                    startActivity(intent)
                    true
                }

                R.id.nav_centroAncona -> {
                    val intent = Intent (this, CentroActivity::class.java)
                    intent.putExtra("centroSportivoId", "ge42drMdBlsI3MUuVeuX")
                    intent.putExtra("nomecentro", "Ancona")
                    startActivity(intent)
                    true
                }

                R.id.nav_centroMacerata -> {
                    val intent = Intent (this, CentroActivity::class.java)
                    intent.putExtra("centroSportivoId", "gPNgSKQgkrg8G0yXmWV4")
                    intent.putExtra("nomecentro", "Macerata")
                    startActivity(intent)
                    true
                }

                R.id.nav_centroFermo -> {
                    val intent = Intent (this, CentroActivity::class.java)
                    intent.putExtra("centroSportivoId", "ixvptNZVldQwyKAMTYkD")
                    intent.putExtra("nomecentro", "Fermo")
                    startActivity(intent)
                    true
                }

                R.id.nav_centroAscoliPiceno -> {
                    val intent = Intent (this, CentroActivity::class.java)
                    intent.putExtra("centroSportivoId", "EHRgWL3MllLaSuWPzddd")
                    intent.putExtra("nomecentro", "Ascoli Piceno")
                    startActivity(intent)
                    true
                }

                R.id.nav_superadminlogout -> {
                    viewmodel.logOut()
                    startActivity(Intent(this, AccediActivity::class.java))

                    true
                }
                else -> {true}
            }
        }
    }
}
