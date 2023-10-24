package com.example.padelmarcheofficial.dataclass

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*



class OperazioniSuFb {
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


    /**
     * Funzione per inserire nuovo utente in *FirebaseAuth*
     * @param acc l'account da inserire in *FirebaseAuth*
     */
    fun inserimentoNuovoAccountInFirebaseAuth(acc: Account) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(acc.email.value.toString(), acc.psw.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registrazione avvenuta, aggiorno i dati dell'utente
                    val user = auth.currentUser
                    acc.idD=user!!.uid
                    //Se le credenziali vengono inserite correttamente, allora salvo le info dell'account nel db
                    inserimentoInFirestore(acc)
                    //Invio l'email di verifica
                   // inviaEmail(user)
                }
            }

    }

    /**
     * Funzione per inviare email di verifica
     * @param user l'utente *FirebaseUser* al quale rinviare l'email di verifica
*/
    fun inviaEmail(user: FirebaseUser) {
    user.sendEmailVerification()
    }

    /**
     * Funzione per inserire in *Firestore* l'account
     * @param acc l'account da inserire in *Firestore*
     */
    private fun inserimentoInFirestore(acc: Account) {
        //inserisco tutti i dati in un hashMap che poi carico sul server
        val profilo = hashMapOf(
            "nome"              to acc.nome.value.toString(),
            "cognome"           to acc.cognome.value.toString(),
            "dataDiNascita"     to acc.compleanno.value.toString(),
            "cellulare"         to acc.cellulare.value.toString(),
            "sesso"             to acc.sesso.value.toString(),
        //    "verificato"        to acc.seiVerificato,
            "presenzaImg"       to acc.presenzaImg // so in dubbio
        )
        //Decommentare per verificare i dati presenti nella variabile acc
        acc.stampa()
        //Il documento ha nome pari all'id in Firebase.auth
        db.collection("Accounts").document(acc.idD)
            .set(profilo)
            .addOnSuccessListener {
                //Se presente l'immagine, la salvo nello storage
                //if (acc.presenzaImg) {
                salvaImg(acc.imgbitmap.value, acc)
                //}
            }
    }
    /**
     * Funzione per inserire nello *storage* di *Firebase* l'immagine profilo
     * @param imgBTM l'immagine Bitmap da caricare nello *storage*
     * @param acc l'account al quale è associata l'immagine
     */
    private fun salvaImg(imgBTM: Bitmap?= null,acc: Account) {
        //Directory della posizione della foto profilo
        val fotoProfiloRef = storageRef.child("Foto profilo/${acc.idD}")
        //acc.check = true
        //se è presente una foto che deve essere eliminata la elimino e resetto la variabile rimossa
        try {
            fotoProfiloRef.delete()
        } catch (exc: Exception) {}
        if(imgBTM!=null) {
            val baos = ByteArrayOutputStream()
            imgBTM.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            fotoProfiloRef.putBytes(baos.toByteArray()).addOnSuccessListener {}
        }

    }
    /**
     * Funzione per richiedere reset password
     * @param email l'email al quale inviare una email di reset password
     * @param cont il context sul quale poter apllicare un Toast.makeText
     */
    fun recuEmail(email: String, cont: Context) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful)
                Toast.makeText(cont,"Recupero avvenuto, controlla le email in arrivo", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(cont,"Recupero fallito, controlla l'email inserita e ritenta",Toast.LENGTH_LONG).show()
        }
    }
    /**
     * Funzione per recuperare le informazioni dell'account
     * @param account l'account contenente l'id del quale richiedere le informazioni
     * @return una mutableMap contenente tutte le informazioni scaricate da *Firestore* relative all'account
     */
    suspend fun scaricaInformazioniAccount(account: Account): MutableMap<String, Any>? {
        val docRef = db.collection("Accounts").document(account.idD).get().await()
        return docRef.data
    }
    /**
     * Scarica dallo *storage* di *Firebase* l'immagine profilo dell'account passato come parametro
     * @param acc l'account del quale ottenere l'immagine
     */
    suspend fun caricaImg(acc: Account) {
        var cosa: ByteArray = ByteArray(0)
        var cosa2: ByteArray = ByteArray(0)
        val pathReference = storageRef.child("/Foto profilo/" + acc.idD)
        val pathReference2 = storageRef.child("/Foto profilo/0.png")
        if (acc.presenzaImg) {
            val cosa = pathReference.getBytes(oneMGBYTE).await()
            acc._imgbitmap.value = BitmapFactory.decodeByteArray(cosa, 0, cosa.size)
        } else
            acc._imgbitmap.value = null
    }
    /**
     * Elimina l'account passato come argomento dalla raccolta di utenti in *Firestore*
     * @param acc l'account da rimuovere
     */
  /* fun aggiornaAccount(acc: Account) {
        db.collection("Accounts").document(acc.idD)
            .delete()
            .addOnSuccessListener {
                UserValue().set(auth.currentUser!!,
                    id = acc.idD,
                    nome = acc.nome.value!!,
                    cognome = acc.cognome.value!!,
                    email = acc.email.value!!,
                    cellulare = acc.cellulare.value!!,
                    sesso = acc.sesso.value!!,

                    inserimentoInFirestore(acc))
            }
    } */
    /**
     * Scarica da *Firestore* i dati relativi all'account e li inserisce all'interno della classe
     * **[UserValue]**, richiamabili in tutte le diverse istanze di classe
     */
    suspend fun initUservalue():Boolean{
        try {
            val docRef = db.collection("Accounts").document(auth.currentUser!!.uid).get().await()
            val data = docRef.data!!
            UserValue().set(user = auth.currentUser!!,
                id = auth.currentUser!!.uid,
                nome = data["nome"].toString(),
                cognome = data["cognome"].toString(),
                email = auth.currentUser!!.email!!,
                cellulare = data["cellulare"].toString(),
                sesso = data["sesso"].toString())
            return true
        }catch (e: Exception){
            return false
        }
    }






    /**
     * Funzione che restituisce la lista degli utenti che contengono la stringa passata come parametro
     * @param nomeRicercato nome e/o cognome da cercare
     * @Return: MutableList di MutableMaps che contengono i nomi e cognomi, corso e classe, id, matricola e immagini degli utenti selezionati
     */
    suspend fun ricercaUtenti(nomeRicercato: String): MutableList<MutableMap<String, Any>> {
        val listacompleta = mutableListOf<MutableMap<String, Any>>()
        //Ricerca case insensitive
        val data = db.collection("Accounts").get().await()
        for (document in data) {
            //rimuovo gli spazi all'interno del nome e del cognome, creo due varianti: nome+cognome
            // e cognome+nome per soddisfare la ricerca con nome e cognome intercambiabili. Elimino
            // gli spazi anche nel nome ricercato dall'utente
            val nomeSanificato = document.getString("nome").toString().replace(" ","").toLowerCase(Locale.ROOT)
            val cognomeSanificato = document.getString("cognome").toString().replace(" ", "").toLowerCase(Locale.ROOT)
            val nomeVersoNormale = nomeSanificato + cognomeSanificato
            val nomeVersoContrario = cognomeSanificato + nomeSanificato
            val senzaSpazi = nomeRicercato.replace(" ", "").toLowerCase(Locale.ROOT)
            //Se almeno una delle due varianti contiene il nome ricercato salvo le sue variabili e
            // le aggiungo alla lista da restituire
            if (nomeVersoNormale.contains(senzaSpazi)||nomeVersoContrario.contains(senzaSpazi)){
                val utente: MutableMap<String, Any> = mutableMapOf()
                utente["nome"] = document.getString("nome").toString() + " " + document.getString("cognome").toString()
                utente["corso"] = document.getString("idCorso").toString() + " " + document.getString("idClasse").toString()
                utente["id"] = document.id
                utente["matricola"] = document.getString("matricola").toString()
                try {
                    val imgdata = storageRef.child("/Foto profilo/" + document.id).getBytes(oneMGBYTE).await()
                    utente["immagine"] = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.size)
                    listacompleta.add(utente)
                }
                catch (e: StorageException) {
                    val stockimg = storageRef.child("/Foto profilo/0.png").getBytes(oneMGBYTE).await()
                    utente["immagine"] = BitmapFactory.decodeByteArray(stockimg, 0, stockimg.size)
                    listacompleta.add(utente)
                }
            }
        }
        return listacompleta
    }







}