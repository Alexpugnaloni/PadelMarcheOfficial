package com.example.padelmarcheofficial.dataclass

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
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
                        listautenti = listaUtenti,
                    )
                    prenotazioniList.add(prenotazione)
                }
            }
        }

        return prenotazioniList
    }
    suspend fun downloadAmministratori(): List<Amministratori> {

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
    suspend fun eliminaPrenotazione(idPrenotazione: String) {
        val centriSportiviRef = db.collection("Centrisportivi")

        try {
            val snapshot = centriSportiviRef.get().await()
            for (document in snapshot) {
                val prenotazioniRef = document.reference.collection("Prenotazioni")
                val prenotazioneDaEliminare = prenotazioniRef.document(idPrenotazione).get().await()

                if (prenotazioneDaEliminare.exists()) {
                    prenotazioniRef.document(idPrenotazione).delete().await()
                }
            }
        } catch (e: Exception) {
            // Gestisci l'eccezione se si verifica un errore durante l'eliminazione della prenotazione
        }
    }

    suspend fun downloadPrenotazioniAmministratore(): List<PrenotazioneAdmin> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email ?: ""
        val prenotazioniList = mutableListOf<PrenotazioneAdmin>()

        // Ottieni i documenti dei centri sportivi che corrispondono all'email dell'utente loggato
        val centriSportiviSnapshot = db.collection("Centrisportivi")
            .whereEqualTo("email", userEmail)
            .get()
            .await()

        for (centroSportivo in centriSportiviSnapshot) {
            val centroSportivoEmail = centroSportivo.getString("email") ?: ""
            val centroSportivoId = centroSportivo.id

            // Ottieni i documenti dalla raccolta "Prenotazioni" di questo centro sportivo
            val prenotazioniSnapshot = db.collection("Centrisportivi")
                .document(centroSportivoId)
                .collection("Prenotazioni")
                .get()
                .await()

            val now = Calendar.getInstance().time // Ottieni la data attuale

            for (prenotazioneDoc in prenotazioniSnapshot) {
                val dataPrenotazione = prenotazioneDoc.getTimestamp("data")?.toDate()
                if (dataPrenotazione != null && dataPrenotazione >= now) {
                    val utenteId = prenotazioneDoc.getString("idutente") ?: ""

                    // Ottieni i dati dell'utente dalla raccolta "Accounts"
                    val accountDoc = db.collection("Accounts").document(utenteId).get().await()
                    val nome = accountDoc.getString("nome") ?: "Admin"
                    val cognome = accountDoc.getString("cognome") ?: ""
                    val cellulare = accountDoc.getString("cellulare") ?: "Admin"




                    val listaUtenti = prenotazioneDoc.get("utenti") as? List<String> ?: emptyList()

                    val prenotazione = PrenotazioneAdmin(
                        id = prenotazioneDoc.id,
                        utente = utenteId,
                        nomeutente = "$nome $cognome",
                        centroSportivo = centroSportivoEmail,
                        date = dataPrenotazione,
                        listautenti = listaUtenti,
                        cellulareUtente = cellulare
                    )
                    prenotazioniList.add(prenotazione)
                }
            }
        }

        // Ordina le prenotazioni per data (dal più recente al meno recente)
        prenotazioniList.sortByDescending { it.date }

        return prenotazioniList
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

    suspend fun contaUtentiStatistiche(): Int {

        return try {
            val querySnapshot: QuerySnapshot = db.collection("Accounts").get().await()
            querySnapshot.size()
        } catch (e: Exception) {

            e.printStackTrace()
            -1
        }
    }
    suspend fun contaPrenotazioniOggi(centroSportivoId: String): Int {

        val today = Calendar.getInstance()

        return try {
            val prenotazioniSnapshot: QuerySnapshot = db.collection("Centrisportivi")
                .document(centroSportivoId)
                .collection("Prenotazioni")
                .get()
                .await()

            var count = 0

            for (prenotazioneDoc in prenotazioniSnapshot) {
                val dataPrenotazione = prenotazioneDoc.getTimestamp("data")
                if (dataPrenotazione != null) {
                    val cal = Calendar.getInstance()
                    cal.time = dataPrenotazione.toDate()
                    if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        cal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                        cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)
                    ) {
                        count++
                    }
                }
            }
            count
        } catch (e: Exception) {

            e.printStackTrace()
            -1
        }
    }

    suspend fun contaPrenotazioniSettimana(centroSportivoId: String): Int {

        val today = Calendar.getInstance()
        val oneWeekAgo = Calendar.getInstance()
        oneWeekAgo.add(Calendar.DAY_OF_YEAR, -7)

        return try {
            val prenotazioniSnapshot: QuerySnapshot = db.collection("Centrisportivi")
                .document(centroSportivoId)
                .collection("Prenotazioni")
                .get()
                .await()

            var count = 0

            for (prenotazioneDoc in prenotazioniSnapshot) {
                val dataPrenotazione = prenotazioneDoc.getTimestamp("data")
                if (dataPrenotazione != null) {
                    val cal = Calendar.getInstance()
                    cal.time = dataPrenotazione.toDate()
                    if (cal.after(oneWeekAgo) && cal.before(today)) {
                        count++
                    }
                }
            }
            count
        } catch (e: Exception) {
            // Gestione dell'eccezione in caso di errore
            e.printStackTrace()
            -1 // Valore di ritorno in caso di errore
        }
    }

    suspend fun contaPrenotazioniMese(centroSportivoId: String): Int {

        val today = Calendar.getInstance()
        val oneMonthAgo = Calendar.getInstance()
        oneMonthAgo.add(Calendar.MONTH, -1)

        return try {
            val prenotazioniSnapshot: QuerySnapshot = db.collection("Centrisportivi")
                .document(centroSportivoId)
                .collection("Prenotazioni")
                .get()
                .await()

            var count = 0

            for (prenotazioneDoc in prenotazioniSnapshot) {
                val dataPrenotazione = prenotazioneDoc.getTimestamp("data")
                if (dataPrenotazione != null) {
                    val cal = Calendar.getInstance()
                    cal.time = dataPrenotazione.toDate()
                    if (cal.after(oneMonthAgo) && cal.before(today)) {
                        count++
                    }
                }
            }
            count
        } catch (e: Exception) {
            // Gestione dell'eccezione in caso di errore
            e.printStackTrace()
            -1 // Valore di ritorno in caso di errore
        }
    }

}





