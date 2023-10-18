package com.example.padelmarcheofficial.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.padelmarcheofficial.PrenotaUnaPartitaActivity
import com.example.padelmarcheofficial.RegistratiActivity
import com.example.padelmarcheofficial.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val model = HomeViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var bottomNav = binding.bottomNavigationView
        setContentView(binding.root)
     //   binding.bottomNavigationView.selectedItemId(R.id.ic_home,true)  //C'ERA SELECTTABBYID
      //  replaceFragment(homepageFragment) // La home si aprirà sul fragment principale


    /*    bottomNav.setOnTabSelectListener(object : BottomNavigationView {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: BottomNavigationView.Tab?,
                newIndex: Int,
                newTab: BottomNavigationView.Tab
            ) {
                //redirecting fragment
                when(newIndex){
                    0 -> replaceFragment(homepageFragment) //CI VA LA NOSTRA HOME
                    1 -> replaceFragment(storageFragment) //CI ANDRANNO LE PRENOTAZIONI
                    2 -> replaceFragment(ricettePreferiteFragment) //CI ANDRA' IL PROFILO

                    else -> replaceFragment(this@HomeActivity.homepageFragment) //DI NUOVO LA HOME
                }


            }


        }) */

        binding.prenotaunapartitaButton.setOnClickListener{
            val intent = Intent(this, PrenotaUnaPartitaActivity::class.java)
            startActivity(intent)
        }

    /*    binding.prenotasolitariaButton.setOnClickListener {
            val intent = Intent(this, PrenotaSolitariaActivity::class.java)  DA CREARE LA CLASSE
            startActivity(intent)
        } */

    }

  /*  binding.aggToolbar.setOnMenuItemClickListener {
        when (it.itemId) {
            R.id.ic_profilo ->  openProfilo()       SONO I TRE BOTTONI IN ALTO A DX, UTILE PER COLLEGAMENTO A PROFILO
            R.id.ic_logout -> {                     E LOGOUT
                model.logOut()
                startActivity(Intent(this, InizioActivity::class.java))
                finish()
            }
        }
        true
    } */


    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
         //   replace(R.id.nav_host_fragment, fragment)
            commit()
        }
    }

   /* private fun openProfilo(){
        startActivity(Intent(this, ProfiloActivity::class.java))
        finish()
    } */

}