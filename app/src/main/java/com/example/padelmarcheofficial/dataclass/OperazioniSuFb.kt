package com.example.padelmarcheofficial.dataclass

import android.graphics.Bitmap
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream

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
                    inviaEmail(user)
                }
            }

    }

    /**
     * Funzione per inviare email di verifica
     * @param user l'utente *FirebaseUser* al quale rinviare l'email di verifica

    fun inviaEmail(user: FirebaseUser) {
    user.sendEmailVerification()
    }*/

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
            "verificato"        to acc.seiVerificato,
            "presenzaImg"       to acc.presenzaImg // so in dubbio
        )
        //Decommentare per verificare i dati presenti nella variabile acc
        //acc.stampa()
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
    fun aggiornaAccount(acc: Account) {
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
    }
    /**
     * Scarica da *Firestore* i dati relativi all'account e li inserisce all'interno della classe
     * **[UserValue]**, richiamabili in tutte le diverse istanze di classe
     */
    suspend fun initUservalue():Boolean{
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
                sesso = data["sesso"].toString(),

                )
            return true
        }catch (e: Exception){
            return false
        }
    }
}