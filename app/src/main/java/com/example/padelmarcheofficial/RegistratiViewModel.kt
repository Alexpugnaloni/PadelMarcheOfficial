package com.example.padelmarcheofficial
import android.content.Context
import android.widget.Toast

/**
 * Classe per verificare corretto inserimento dei dati
 */
class RegistratiViewModel(context: Context?=null) {
    private val baseContext = context

    /**
     * Verifica che i dati corrispondenti al nome, cognome non siano vuoti, che l'email rispetti il
     * formato desiderato, la password sia almeno di 8 caratteri, il compleanno rispetti il formato desiderato e
     * il cellulare debba essere corretto
     */
    fun verificaInserimento(
        nome: String,
        cognome: String,
        email: String,
        psw: String,
        compleanno: String,
        cellulare: String): Boolean {

        if (nome.isBlank()) {
            makeToast("Nome mancante", Toast.LENGTH_SHORT)
            return false
        }
        if (cognome.isBlank()) {
            makeToast("Cognome mancante", Toast.LENGTH_SHORT)
            return false
        }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (!email.matches(emailPattern.toRegex())) {
            makeToast("Email non valida", Toast.LENGTH_SHORT)
            return false
        }
        if (psw.isBlank()) {
            makeToast("Password mancante", Toast.LENGTH_SHORT)
            return false
        }
        if (psw.length < 8) {
            makeToast("La password deve avere almeno 8 caratteri", Toast.LENGTH_LONG)
            return false
        }
        val formatoData = "\\d{2}-\\d{2}-\\d{4}".toRegex()
        if (compleanno.isEmpty() || !compleanno.matches(formatoData)) {
            makeToast("Data Mancante o errata", Toast.LENGTH_LONG)
            return false
        }
        val formatoNumeroTelefono = "\\d{10}".toRegex()
        if (cellulare.isBlank()) {
            makeToast("Cellulare Mancante", Toast.LENGTH_SHORT)
            return false
        } else if (!cellulare.matches(formatoNumeroTelefono)) {
            makeToast("Cellulare Errato", Toast.LENGTH_SHORT)
            return false
        } else {
            return true
        }
    }

    /**
     * Se *[baseContext]* è diverso da null allora stampo il messaggio all'interno di un Toast.
     */
    private fun makeToast(testo:String,durata:Int) {
        if(baseContext!=null)
            Toast.makeText(baseContext, testo, durata).show()
    }
}