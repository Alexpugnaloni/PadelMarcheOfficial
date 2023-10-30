package com.example.padelmarcheofficial.dataclass

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.widget.Toast

/**
 * * Classe per usufruire di operazioni funzionali.
 *
 * @author Di Biase Alessandro, Donnini Valerio, Sopranzetti Lorenzo
 */
class Funzionalita{

    companion object {
        /**
         * larghezza dello schermo in Pixel
         */
        private var width : Int = 0
        //ahahahahahaha

        /**
         * altezza dello schermo in Pixel
         */
        private var height: Int = 0
    }

    /**
     * Modifico i valori di larghezza e altezza dello schermo
     * @param width la larghezza dello schermo
     * @param height l'altezza dello schermo
     */
    fun setWidthAndHeight(width:Int,height:Int){
        Funzionalita.width=width
        Funzionalita.height=height
    }

    /**
     * Verifica che sia presenta una connessione internet
     * @param cont Context
     * @return true se il dispositivo risulta collegato
     */
    fun isOnline(cont: Context): Boolean {
        val cm = cont.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if(netInfo != null && netInfo.isConnectedOrConnecting)
            return true
        Toast.makeText(cont, "Non è presente una connessione a internet", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Adatta l'immagine alle dimensioni dello schermo. Adegua la larghezza della foto alla
     * larghezza dello schermo, se l'altezza risulta maggiore dei 3/4 di quella dello schermo viene
     * rimpicciolita al fine di non occupare tutto lo schermo. E' necessario aver settato le
     * variabili **[width]** e **[height]** mediante la funzione **[setWidthAndHeight]**
     * @param immagine l'immagine da ridimensionare
     * @return l'immagine ridimensionata
     */
    fun adattaImage(immagine: Bitmap): Bitmap {
        //calcolo il rapporto della foto
        val rap : Float = immagine.height.toFloat()/immagine.width.toFloat()
        //altezza h e larghezza w della foto adattata a schermo intero
        var h = rap* width
        var w = width.toFloat()
        //se la foto è troppo alta e occupa molto spazio la ridimensione
        if(h> height.toFloat()/4*3){
            h = height.toFloat()/4*3
            w = h/rap
        }
        /**
         * effettua resizing immagine
         */
        return Bitmap.createScaledBitmap(immagine,w.toInt(),h.toInt(),false)
    }
}