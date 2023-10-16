package com.example.padelmarcheofficial.dataclass

import com.google.firebase.auth.FirebaseUser


/**
 * * Classe per ottenere i dati utente solo istanziandola
 *
 * @author
 */
class UserValue {
    companion object {
        /**
         * Variabile *FirebaseUser* relativa all'utente
         */
        private var user: FirebaseUser? = null

        /**
         * Id dell'utente
         */
        private var id: String = ""

        /**
         * Nome dell'utente
         */
        private var nome: String = ""

        /**
         * Cognome dell'utente
         */
        private var cognome: String = ""

        /**
         * Email dell'utente
         */
        private var email: String = ""

        /**
         * Data di nascita dell'utente
         */
        private var compleanno = ""

        /**
         * Cellulare dell'utente
         */
        private var cellulare: String = ""

        /**
         * Genere dell'utente
         */
        private var sesso: String = ""


    }

    /**
     * Assegna valori alle variabili
     * @param user      da assegnare a **[UserValue.user]**
     * @param id        da assegnare a **[UserValue.id]**
     * @param nome      da assegnare a **[UserValue.nome]**
     * @param cognome   da assegnare a **[UserValue.cognome]**
     * @param email     da assegnare a **[UserValue.email]**
     * @param cellulare da assegnare a **[UserValue.cellulare]**
     * @param sesso     da assegnare a **[UserValue.sesso]**

     */
    fun set(user: FirebaseUser? = null, id: String, nome: String, cognome: String, corso: String,
        classe: String, email: String, cellulare: String, sesso: String
    ) {
        UserValue.user = user
        UserValue.id = id
        UserValue.nome = nome
        UserValue.cognome = cognome
        UserValue.email = email
        UserValue.cellulare = cellulare
        UserValue.sesso = sesso
    }

    /**
     * Assegna valori della variabile account alle variabili interne della classe
     * @param account l'account contenente le informazioni
     * @param user parametro per il recupero dell'id
     */
    fun set(account: Account, user: FirebaseUser?) {
        UserValue.user  = user
        id              = user!!.uid
        nome            = account._nome.value.toString()
        cognome         = account._cognome.value.toString()
        email           = user.email!!
        compleanno      = account._compleanno.value.toString()
        cellulare       = account.cellulare.value.toString()
        sesso           = account.sesso.value.toString()

    }

}
