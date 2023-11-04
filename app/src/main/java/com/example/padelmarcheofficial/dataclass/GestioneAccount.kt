package com.example.padelmarcheofficial.dataclass

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Looper
import android.util.Log
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.logging.Handler

class GestioneAccount {

    /**
     * Riferimento per *Firestore*
     */

    private val db = Firebase.firestore

    /**
     * Riferimento per *FirebaseAuth*
     */
    private var auth: FirebaseAuth = Firebase.auth

    /**
     * Lo *storage* di *Firebase*
     */
    private val storage = Firebase.storage

    /**
     * Riferimento per lo *storage* di *Firebase*
     */
    private var storageRef = storage.reference

    /**
     * Ridimensionamento immagini per il caricamento
     */
    private val oneMGBYTE: Long = 1024 * 1024 * 5



    fun isOnline(cont: Context): Boolean {
        val cm = cont.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting)
            return true
        Toast.makeText(cont, "Non è presente una connessione a internet", Toast.LENGTH_SHORT).show()
        return false
    }

    fun inserisciAccount(acc: Account) {

        auth.createUserWithEmailAndPassword(acc.email.value.toString(), acc.psw.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registrazione avvenuta, aggiorno i dati dell'utente
                    val user = auth.currentUser
                    acc.idD = user!!.uid
                    //inserisco tutti i dati in un hashMap che poi carico sul server
                    val profilo = hashMapOf(
                        "nome" to acc.nome.value.toString(),
                        "cognome" to acc.cognome.value.toString(),
                        "dataDiNascita" to acc.compleanno.value.toString(),
                        "cellulare" to acc.cellulare.value.toString(),
                        "sesso" to acc.sesso.value.toString(),
                        "presenzaImg" to acc.presenzaImg // so in dubbio
                    )
                    Log.d("Operazione", "Ora lo inserisco")

                    //Il documento ha nome pari all'id in Firebase.auth
                    db.collection("Accounts").document(acc.idD)
                        .set(profilo)
                        .addOnSuccessListener {
                            //Se presente l'immagine, la salvo nello storage
                            //if (acc.presenzaImg) {
                            Log.d("Operazione", "sono dentro il success")
                            salvaImg(acc.imgbitmap.value, acc)
                            //}
                        }
                        .addOnFailureListener { exception ->
                            Log.d("Operazione", exception.toString())
                        }

                    Log.d("Operazione", "ora sono fuori")
                } else {
                    Log.d("Operazione", "Ora lo inserisco1")
                }
            }
    }

    private fun salvaImg(imgBTM: Bitmap? = null, acc: Account) {
        //Directory della posizione della foto profilo
        val fotoProfiloRef = storageRef.child("Foto profilo/${acc.idD}")
        //acc.check = true
        //se è presente una foto che deve essere eliminata la elimino e resetto la variabile rimossa
        try {
            fotoProfiloRef.delete()
        } catch (exc: Exception) {
        }
        if (imgBTM != null) {
            val baos = ByteArrayOutputStream()
            imgBTM.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            fotoProfiloRef.putBytes(baos.toByteArray()).addOnSuccessListener {}
        }

    }


    internal suspend fun downloadSedi(): HashMap<String, CentroSportivo> {
        val centriPadel: HashMap<String, CentroSportivo> = hashMapOf()
        val snapshot = db.collection("Centrisportivi").get().await()
        for (doc in snapshot)
            centriPadel.put(
                doc["sede"].toString(),
                CentroSportivo(
                    doc.id,
                    doc["sede"].toString(),
                    doc["indirizzo"].toString(),
                    doc["civico"].toString()
                )
            )
        return centriPadel
    }

    internal suspend fun downloadNomiSedi(): List<String> {
        val centriPadelList = mutableListOf<String>()
        val snapshot = db.collection("Centrisportivi").get().await()
        for (doc in snapshot)
            centriPadelList.add(doc["sede"].toString())
        return centriPadelList.toList()
    }

    suspend fun downloadPrenotazioni(centroSportivo: String, data: Date): List<Prenotazione> {
        val prenotazioniList = mutableListOf<Prenotazione>()
        val snapshot =
            db.collection("Centrisportivi").document(centroSportivo).collection("Prenotazioni")
                .whereGreaterThanOrEqualTo("data", data).get().await()
        for (doc in snapshot) {
            var listautenti: List<String>
            if (!doc.getBoolean("confermato")!!) {
                listautenti = doc["utenti"] as List<String>
            } else
                listautenti = listOf("", "", "")
            prenotazioniList.add(
                Prenotazione(
                    doc.id,
                    doc["idutente"].toString(),
                    centroSportivo,
                    doc.getDate("data") as Date,
                    listautenti
                )
            )
        }

        return prenotazioniList.toList()

    }

    fun uploadPrenotazione(idcentrosportivo: String, data: Date, solitaria: Boolean) {
        val prenotazione = hashMapOf(
            "idutente" to auth.currentUser!!.uid,
            "data" to data,
            "confermato" to !solitaria,
            "utenti" to listOf<String>(),

            )
        db.collection("Centrisportivi").document(idcentrosportivo).collection("Prenotazioni")
            .add(prenotazione)
    }

    suspend fun updatePrenotazione(idcentrosportivo: String, prenotazione: Prenotazione, context: Context) {
        db.runTransaction {
            val prenotazione = db.collection("Centrisportivi").document(idcentrosportivo)
                .collection("Prenotazioni").document(prenotazione.id)
            val snapshot = it.get(prenotazione)
            val listautenti = snapshot["utenti"] as MutableList<String>
            if (listautenti.size == 3)
                Toast.makeText(
                    context,
                    "Troppo tardi! Ormai gli utenti sono al completo",
                    Toast.LENGTH_LONG
                ).show()
            else {
                listautenti.add(auth.uid.toString())
                it.update(prenotazione, "utenti", listautenti)
                if (listautenti.size == 3)
                    it.update(prenotazione, "confermato", true)
            }
        }.await()
    }

    fun getIdUtente(): String {
        return auth.uid.toString()
    }
}





