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
import com.example.padelmarcheofficial.dataclass.GestioneAccount
import com.example.padelmarcheofficial.dataclass.OperazioniSuFb
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class RegistratiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private  var operazioniSuFb = OperazioniSuFb()
    private lateinit var btnReg: Button
    private lateinit var btnBack: Button

    private val acc : Account by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


       /* binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.progressBar.visibility = ProgressBar.VISIBLE */

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

      /*  binding.button.setOnClickListener{
            val intent = Intent(this, AccediActivity::class.java)
            startActivity(intent)

        }*/
        //------NOME
        binding.textFieldname.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(1, binding.textFieldname.editText?.text?.toString())// Respond to input text change
        }
        //------COGNOME
        binding.textFieldsurname.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(2, binding.textFieldsurname.editText?.text.toString())// Respond to input text change
        }
        //------EMAIL
        binding.textFieldusername.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(3, binding.textFieldusername.editText?.text.toString())// Respond to input text change
        /*    if (binding.textFieldusername.editText?.text.toString().isNotEmpty() && !binding.textFieldusername.editText?.text.toString().endsWith("@studenti.univpm.it")){
                binding.textFieldusername.isErrorEnabled = true
                binding.textFieldusername.error = "email non valida"
            }else {
                binding.textFieldusername.error = null
                binding.textFieldusername.isErrorEnabled =false
            }*/
        }
//------PASSWORD
        binding.textFieldpassword.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(4, binding.textFieldpassword.editText?.text.toString())// Respond to input text change
        }
//------COMPLEANNO
        binding.textFieldeditTextDate.editText?.doOnTextChanged { inputText, before, _, _ ->
            val finalCursorPosition= binding.textFieldeditTextDate.editText?.selectionStart!!
            if((inputText?.length==2||inputText?.length==5)&&finalCursorPosition>before) {
                binding.textFieldeditTextDate.editText?.setText("${binding.textFieldeditTextDate.editText!!.text}-")
                binding.textFieldeditTextDate.editText?.setSelection(binding.textFieldeditTextDate.editText?.text.toString().length)
            }
            acc.changeValue(5, binding.textFieldeditTextDate.editText?.text.toString())// Respond to input text change
        }

        //------CELLULARE
        binding.textFieldphone.editText?.doOnTextChanged { _, _, _, _ ->
            acc.changeValue(6, binding.textFieldphone.editText?.text?.toString())// Respond to input text change
        }


//------CLASSE

        val spinner: Spinner = findViewById(R.id.spinnerSesso)
// Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.sesso_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }

       // spinnerG = findViewById(R.id.spinnerGroup)
        binding.spinnerSesso.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                acc.changeValue(7, binding.spinnerSesso.selectedItem.toString())
            }
        }
    /*    val gruppoObserver = Observer<String> { newValue ->
            for(i in 0 until listClassi.size) {
                if (newValue == listClassi[i]) {
                    binding.spinnerGroup.setSelection(i)
                    binding.spinnerGroup.post {
                        if (i < binding.spinnerGroup.size)
                            binding.spinnerGroup.setSelection(i)
                    }
                    break
                }
            }
        }
        acc.idClasse.observe(this,gruppoObserver) */
//------IMMAGINE
  /*      imgAccount = findViewById<>(R.id.imgAccount)
        val imageObserver = Observer<Bitmap> { newValue ->
            binding.imgAccount.setImageBitmap(newValue)
        }
        acc.imgbitmap.observe(this, imageObserver)

        imgAccount.setImageResource(R.drawable.ic_baseline_account_circle_24)
        imgAccount.clipToOutline = true
        imgAccount.scaleType = ImageView.ScaleType.CENTER_CROP
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        imgAccount.setOnClickListener {
            val intent=Intent(this, ManagingPHOTO::class.java)
            intent.putExtra("Rounded",true)
            startActivityForResult(intent,managingPHOTO)
        } */

        //funzione utilizzata per la registrazione previa check delle informazioni inserite
        //per la registrazione viene richiamata la funzione della classe Account che va ad inserire l'account su firebase
        btnReg = findViewById<Button>(R.id.button)
        btnReg.setOnClickListener {
            if(RegistratiViewModel(baseContext).verificaInserimento(binding.name.text.toString(), binding.surname.text.toString(), binding.username.text.toString(), binding.password.text.toString(),binding.editTextDate.toString(), binding.phone.text.toString())){
            //    if(spinnerC.selectedItem != null&& spinnerC.selectedItem.toString().isNotBlank()&&spinnerC.selectedItemPosition!=0){
              //      if(spinner.selectedItem != null&& spinner.selectedItem.toString().isNotBlank()&&spinner.selectedItemPosition!=0){
                     val gestioneAccount = GestioneAccount()
                Log.d("NOME", acc.nome.value.toString())
                Log.d("COGNOME", acc.cognome.value.toString())
                Log.d("EMAIL", acc.email.value.toString())
                Log.d("PASSWORD", acc.psw.value.toString())
                Log.d("COMPLEANNO", acc.compleanno.value.toString())

                Log.d("CELLULARE",acc.cellulare.toString())
                Log.d("SESSO", acc.sesso.toString())
                        gestioneAccount.inserisciAccount(acc)
               // if (it.isSuccessful){
                    val intent = Intent(this, AccediActivity::class.java)
                    startActivity(intent)
             //   }



                    }
                 //   else
                  //      Toast.makeText(baseContext, "Anno iscrizione mancante",Toast.LENGTH_SHORT).show()
                }
             //   else
              //      Toast.makeText(baseContext, "Corso mancante",Toast.LENGTH_SHORT).show()
        btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, AccediActivity::class.java)
            startActivity(intent)
        }
            }


        }/*
    }
          binding.button.setOnClickListener{
          /*  val name = binding.name.text.toString()
            val surname = binding.surname.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val compleanno = binding.editTextDate.text.toString()
            val cellulare = binding.phone.text.toString()
            val sesso = binding.spinnerSesso.toString()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                operazioniSuFb.inserimentoNuovoAccountInFirebaseAuth()
                    if (it.isSuccessful){
                        val intent = Intent(this, AccediActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

            } else Toast.makeText(this, "Completa i campi", Toast.LENGTH_SHORT).show() */
        }

    }
}*/