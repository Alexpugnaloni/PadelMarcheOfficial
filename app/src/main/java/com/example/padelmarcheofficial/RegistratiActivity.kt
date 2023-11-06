package com.example.padelmarcheofficial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.padelmarcheofficial.databinding.ActivityRegisterBinding
import com.example.padelmarcheofficial.dataclass.Account
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.google.firebase.auth.FirebaseAuth

/**
 * Classe che gestisce la registrazione di un nuovo utente
 */
@Suppress("DEPRECATION")
class RegistratiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private  var gestioneFirebase = GestioneFirebase()
    private lateinit var btnReg: Button
    private lateinit var btnBack: Button

    private val acc : Account by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //------NOME
        binding.textFieldname.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(1, binding.textFieldname.editText?.text?.toString())
        }
        //------COGNOME
        binding.textFieldsurname.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(2, binding.textFieldsurname.editText?.text.toString())
        }
        //------EMAIL
        binding.textFieldusername.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(3, binding.textFieldusername.editText?.text.toString())

        }
        //------PASSWORD
        binding.textFieldpassword.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(4, binding.textFieldpassword.editText?.text.toString())
        }

        //------COMPLEANNO
        binding.textFieldeditTextDate.editText?.doOnTextChanged { inputText, before, _, _ ->
            val finalCursorPosition= binding.textFieldeditTextDate.editText?.selectionStart!!
            if((inputText?.length==2||inputText?.length==5)&&finalCursorPosition>before) {
                binding.textFieldeditTextDate.editText?.setText("${binding.textFieldeditTextDate.editText!!.text}-")
                binding.textFieldeditTextDate.editText?.setSelection(binding.textFieldeditTextDate.editText?.text.toString().length)
            }
            acc.changeValue(5, binding.textFieldeditTextDate.editText?.text.toString())
        }

        //------CELLULARE
        binding.textFieldphone.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(6, binding.textFieldphone.editText?.text?.toString())
        }


        //------SESSO

        val spinner: Spinner = findViewById(R.id.spinnerSesso)

        ArrayAdapter.createFromResource(
            this,
            R.array.sesso_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        binding.spinnerSesso.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                acc.changeValue(7, binding.spinnerSesso.selectedItem.toString())
            }
        }

        //funzione utilizzata per la registrazione previa check delle informazioni inserite

        btnReg = findViewById<Button>(R.id.button)
        btnReg.setOnClickListener {
            if(RegistratiViewModel(baseContext).verificaInserimento(binding.name.text.toString(), binding.surname.text.toString(), binding.username.text.toString(), binding.password.text.toString(),binding.editTextDate.text.toString(), binding.phone.text.toString())){

                     val gestioneFirebase = GestioneFirebase()
                Log.d("NOME", acc.nome.value.toString())
                Log.d("COGNOME", acc.cognome.value.toString())
                Log.d("EMAIL", acc.email.value.toString())
                Log.d("PASSWORD", acc.psw.value.toString())
                Log.d("COMPLEANNO", acc.compleanno.value.toString())
                Log.d("CELLULARE",acc.cellulare.toString())
                Log.d("SESSO", acc.sesso.toString())
                        gestioneFirebase.inserisciAccount(acc)

                    val intent = Intent(this, AccediActivity::class.java)
                    startActivity(intent)
                    }

                }

        btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, AccediActivity::class.java)
            startActivity(intent)
        }
    }
}