package com.example.padelmarcheofficial.dataclass

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.security.Timestamp
import java.util.Calendar
import java.util.Date

class GestioneFirebase {

    /**
     * Riferimento per *Firestore*
     */

    private val db = Firebase.firestore

    /**
     * Riferimento per *FirebaseAuth*
     */
    private var auth: FirebaseAuth = Firebase.auth

    /**
     * Funzione per verificare se il dispositivo è online
     */
    fun isOnline(cont: Context): Boolean {
        val cm = cont.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting)
            return true
        Toast.makeText(cont, "Non è presente una connessione a internet", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Funzione per recuperare le informazioni dell'account contenente l'id del quale richiedere le informazioni
     * ritorna una mutableMap contenente tutte le informazioni scaricate da *Firestore* relative all'account
     */
    suspend fun scaricaInformazioniAccount(account: Account): MutableMap<String, Any>? {
        val docRef = db.collection("Accounts").document(account.idD).get().await()
        return docRef.data
    }

    /**
     * Scarica da *Firestore* i dati relativi all'account e li inserisce all'interno della classe

     */
    suspend fun initUservalue(): Boolean {
        try {
            val docRef = db.collection("Accounts").document(auth.currentUser!!.uid).get().await()
            val data = docRef.data!!
            UserValue().set(
                user = auth.currentUser!!,
                id = auth.currentUser!!.uid,
                nome = data["nome"].toString(),
                cognome = data["cognome"].toString(),
                email = auth.currentUser!!.email!!,
                cellulare = data["cellulare"].toString(),
                sesso = data["sesso"].toString()
            )
            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Funzione che registra il nuovo utente tramite username e password ed crea un hashmap
     * con i dati da caricare sul server
     */
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
                    )
                    Log.d("Operazione", "Ora lo inserisco")

                    //Il documento ha nome pari all'id in Firebase.auth
                    db.collection("Accounts").document(acc.idD)
                        .set(profilo)
                        .addOnSuccessListener {
                            Log.d("Operazione", "sono dentro il success")

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

    /**
     * Funzione per inserire in *Firestore* l'account
     */
    private fun inserimentoInFirestore(acc: Account) {
        //inserisco tutti i dati in un hashMap che poi carico sul server
        val profilo = hashMapOf(
            "nome" to acc.nome.value.toString(),
            "cognome" to acc.cognome.value.toString(),
            "dataDiNascita" to acc.compleanno.value.toString(),
            "cellulare" to acc.cellulare.value.toString(),
            "sesso" to acc.sesso.value.toString(),

            )
        acc.stampa()
        db.collection("Accounts").document(acc.idD)
            .set(profilo)
            .addOnSuccessListener {
            }
    }

    /**
     * Funzione per effettuare download delle sedi dei centri sportivi
     */
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
                    doc["civico"].toString(),
                    doc["email"].toString(),
                )
            )
        return centriPadel
    }

    /**
     * Funzione per effettuare download dei nomi delle sedi dei centri sportivi
     */
    internal suspend fun downloadNomiSedi(): List<String> {
        val centriPadelList = mutableListOf<String>()
        val snapshot = db.collection("Centrisportivi").get().await()
        for (doc in snapshot)
            centriPadelList.add(doc["sede"].toString())
        return centriPadelList.toList()
    }

    /**
     * Funzione per effettuare download delle prenotazioni una volta passate come informazioni il
     * centro sportivo e la data
     */
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

    /**
     * Funzione per effettuare upload della prenotazione una volta passato l'id del centro sportivo,
     * la data e se si tratta di una partita privata (singola)
     */
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

    /**
     * Funzione per effettuare l'update della prenotazione una volta passato l'id del centro sportivo e
     * la prenotazione di riferimento
     */
    suspend fun updatePrenotazione(
        idcentrosportivo: String,
        prenotazione: Prenotazione,
        context: Context
    ) {
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

    suspend fun downloadPrenotazioniUtente(): List<Prenotazione> {
        val prenotazioniList = mutableListOf<Prenotazione>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val utenteID = currentUser?.uid ?: ""

        val currentDate = Calendar.getInstance().time

        val centriSportiviSnapshot = db.collection("Centrisportivi").get().await()

        for (centroSnapshot in centriSportiviSnapshot) {
            val centroSportivo = centroSnapshot.id

            val snapshot = db.collection("Centrisportivi")
                .document(centroSportivo)
                .collection("Prenotazioni")
                .whereEqualTo("idutente", utenteID)
                .get()
                .await()

            for (doc in snapshot.documents) {
                val dataPrenotazione = doc.getTimestamp("data")!!.toDate()

                // Verifica se la data della prenotazione è oggi o in futuro
                if (!dataPrenotazione.before(currentDate)) {
                    val utente = doc.getString("idutente") ?: ""
                    val listaUtenti = doc.get("utenti") as? List<String> ?: emptyList()

                    val prenotazione = Prenotazione(
                        id = doc.id,
                        utente = utente,
                        centroSportivo = centroSportivo,
                        date = dataPrenotazione,
                        listautenti = listaUtenti
                    )
                    prenotazioniList.add(prenotazione)
                }
            }
        }

        return prenotazioniList
    }
    suspend fun downloadAmministratori(): List<Amministratori> {
        //val db = FirebaseFirestore.getInstance()
        val amministratoriList = mutableListOf<Amministratori>()

        try {
            val amministratoriSnapshot = db.collection("Amministratori").get().await()

            for (document in amministratoriSnapshot.documents) {
                val id = document.id
                val email = document.getString("email") ?: ""
                val sede = document.getString("sede") ?: ""

                val amministratore = Amministratori(id, email, sede)
                amministratoriList.add(amministratore)
            }
        } catch (e: Exception) {
            // Gestisci l'eccezione
            e.printStackTrace()
        }

        return amministratoriList
    }





    /**
     * restituisce l'id dell'utente
     */
    fun getIdUtente(): String {
        return auth.uid.toString()
    }

    /**
     * Funzione che aggiorna le informazioni dell'account ed effettua l'upload
     * su Firestore
     */
    fun aggiornaAccount(acc: Account) {
        db.collection("Accounts").document(acc.idD)
            .delete()
            .addOnSuccessListener {
                UserValue().set(
                    auth.currentUser!!,
                    id = acc.idD,
                    nome = acc.nome.value!!,
                    cognome = acc.cognome.value!!,
                    email = acc.email.value!!,
                    cellulare = acc.cellulare.value!!,
                    sesso = acc.sesso.value!!,
                )
                inserimentoInFirestore(acc)
            }
    }

}





