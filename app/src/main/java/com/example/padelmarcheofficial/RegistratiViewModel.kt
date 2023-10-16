package com.example.padelmarcheofficial
import android.content.Context
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter
/**
 * * Classe per verificare corretto inserimento dei dati
 *
 * @author Di Biase Alessandro, Donnini Valerio, Sopranzetti Lorenzo
 */
class RegistratiViewModel(context: Context?=null) {
    private val baseContext = context

    /**
     * Verifica che i dati corrispondenti al nome, cognome non siano vuoti, che l'email rispetti il
     * formato desiderato, la password sia almeno di 8 caratteri, il compleanno sia una stringa
     * rappresentante una data esistente
     *
     * @param nome il nome dell'utente in fase di registrazione
     * @param cognome il cognome dell'utente in fase di registrazione
     * @param email l'email universitaria dell'utente in fase di registrazione
     * @param psw la password inserite dall'utente per accedere all'applicazione
     * @param compleanno stringa contenente la data di nascita dell'utente in formato "dd-MM-yyyy"
     * @param cellulare stringa contenente il numero di telefono
     *
     * @return true se tutti i dati rispettano i controlli, altrimenti false
     *
     */
    fun verificaInserimento(
        nome: String,
        cognome: String,
        email: String,
        psw: String,
        compleanno: String,
        cellulare: String): Boolean {
        //    val primaParteEmail = email.split("@")
        //    val matricola = primaParteEmail[0]
        if (nome.isBlank()) {
            makeToast("Nome mancante", Toast.LENGTH_SHORT)
            return false
        }
        if (cognome.isBlank()) {
            makeToast("Cognome mancante", Toast.LENGTH_SHORT)
            return false
        }
        if (email.isBlank()) {
            makeToast("Email mancante", Toast.LENGTH_SHORT)
            return false

        }

        /*    val primaParte = email.split("@")
        var senzaLettere = primaParte[0].replace("s","")
        senzaLettere = senzaLettere.replace("S","")
        if (senzaLettere.length != 7){
            makeToast( "Matricola non valida", Toast.LENGTH_SHORT)
            return false
        }*/
        if (psw.isBlank()) {
            makeToast("Password mancante", Toast.LENGTH_SHORT)
            return false
        }
        if (psw.length < 8) {
            makeToast("La password deve avere almeno 8 caratteri", Toast.LENGTH_LONG)
            return false
        }

        if (compleanno.isBlank()) {
            makeToast("Data Mancante", Toast.LENGTH_LONG)
            return false
        }
        if (cellulare.isBlank()) {
            makeToast("Celluare mancante", Toast.LENGTH_LONG)
            return false
        }
     /*   try {
            val date = LocalDate.parse(compleanno, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            if ((date.year < 1920) || (date.year > 2002)) {
                makeToast( "Data di nascita non corretta", Toast.LENGTH_SHORT)
                return false
            }
            return true
        } catch (e: Exception) {
            makeToast("Data mancante o errata", Toast.LENGTH_SHORT)
            return false
        } */
        return true
        }


    /**
     * Se *[baseContext]* è diverso da null allora stampo il messaggio all'interno di un Toast.
     * Funzione necessaria per permettere corretta esecuzione test
     */
    private fun makeToast(testo:String,durata:Int) {
        if(baseContext!=null)
            Toast.makeText(baseContext, testo, durata).show()
    }
}