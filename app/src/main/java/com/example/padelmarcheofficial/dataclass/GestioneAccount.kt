package com.example.padelmarcheofficial.dataclass

import android.graphics.Bitmap
import android.util.Log
import com.example.padelmarcheofficial.ui.prenotazioni.Prenotazione
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

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


    fun inserisciAccount(acc: Account) {

        auth.createUserWithEmailAndPassword(acc.email.value.toString(), acc.psw.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registrazione avvenuta, aggiorno i dati dell'utente
                    val user = auth.currentUser
                    acc.idD = user!!.uid
                    //inserisco tutti i dati in un hashMap che poi carico sul server
                    val profilo = hashMapOf(
                        "nome"              to acc.nome.value.toString(),
                        "cognome"           to acc.cognome.value.toString(),
                        "dataDiNascita"     to acc.compleanno.value.toString(),
                        "cellulare"         to acc.cellulare.value.toString(),
                        "sesso"             to acc.sesso.value.toString(),
                        "presenzaImg"       to acc.presenzaImg // so in dubbio
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
                }
                else {Log.d("Operazione", "Ora lo inserisco1") }
            }
    }

    private fun salvaImg(imgBTM: Bitmap?= null, acc: Account) {
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

    internal suspend fun downloadNomiSedi(): List<String> {
        val centriPadelList = mutableListOf<String>()
        val snapshot = db.collection("Centrisportivi").get().await()
        for (doc in snapshot)
            centriPadelList.add(doc["sede"].toString())
        return centriPadelList.toList()
    }

    internal suspend fun downloadPrenotazioni(centroSportivo: String, data: String): List<Prenotazione> {
        val prenotazioniList = mutableListOf<Prenotazione>()
        val snapshot = db.collection("Centrisportivi").document(centroSportivo).collection("Prenotazioni")
          //  .whereEqualTo("sede.nome", centroSportivo)
          //  .whereEqualTo("data", data)
            .get()
            .await()

        for (doc in snapshot) {
            val prenotazione = doc.toObject(Prenotazione::class.java)
            prenotazioniList.add(prenotazione)
        }

        return prenotazioniList.toList()
    }


}



