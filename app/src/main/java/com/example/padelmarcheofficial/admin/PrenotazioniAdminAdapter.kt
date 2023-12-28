package com.example.padelmarcheofficial.admin

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.dataclass.PrenotazioneAdmin
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioneClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Classe Adapter per la gestione dell'item di una recycleView delle Prenotazioni per l'admin
 */
class PrenotazioniAdminAdapter(private val prenotazioneClickListener: PrenotazioneClickListener) : ListAdapter<PrenotazioneAdmin, PrenotazioniAdminAdapter.PrenotazioneViewHolder>(PrenotazioneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrenotazioneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_admin_layout, parent, false)
        return PrenotazioneViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrenotazioneViewHolder, position: Int) {
        val currentPrenotazione = getItem(position)
        holder.bind(currentPrenotazione)
    }

    inner class PrenotazioneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val prenotazioneDataTextView: TextView = itemView.findViewById(R.id.prenotazioneDataTextView)
        private val prenotazioneConfermataTextView: TextView = itemView.findViewById(R.id.prenotazioneConfermataTextView)
        private val prenotazioneNomeCognomeUtenteTextView: TextView = itemView.findViewById(R.id.prenotazioneNomeCognomeUtenteTextView)
        private val prenotazioneCellulareTextView:TextView = itemView.findViewById(R.id.prenotazioneCellulareTextView)

        init {
            itemView.findViewById<ImageView>(R.id.btnEliminaPren).setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val prenotazioneToDelete = getItem(position)

                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setTitle("Conferma eliminazione")
                    builder.setMessage("Sei sicuro di voler eliminare questa prenotazione?")
                    builder.setPositiveButton("Elimina") { dialog, _ ->
                        val idPrenotazione = prenotazioneToDelete.id
                        prenotazioneClickListener.onPrenotazioneDelete(idPrenotazione)
                    }
                    builder.setNegativeButton("Annulla") { dialog, _ ->
                        dialog.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
        fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = Date(timestamp)
            return sdf.format(date)
        }
        fun bind(prenotazione: PrenotazioneAdmin) {



            val timestamp = prenotazione.date.time
            val formattedDate = formatTimestamp(timestamp)
            prenotazioneDataTextView.text = "Giorno e ora: " + formattedDate


            val partecipanti: String = when (prenotazione.listautenti.size) {
                0 -> "CONFERMATA"
                1 -> "NON CONFERMATA"
                2 -> "NON CONFERMATA"
                3, 4 -> "CONFERMATA"
                else -> prenotazione.listautenti.size.toString()
            }

            prenotazioneConfermataTextView.text = "Prenotazione: $partecipanti"
            prenotazioneNomeCognomeUtenteTextView.text = "Nome e Cognome: ${prenotazione.nomeutente}"
            prenotazioneCellulareTextView.text = "Cellulare: ${prenotazione.cellulareUtente}"
        }
    }

    /**
     * Classe utilizzata insieme all'adapter di RecyclerView dell'admin per gestire gli aggiornamenti degli elementi
     * visualizzati nella lista in modo efficiente verificando items e contenuti.
     * DiffUtil è una classe fornita da Android Jetpack in grado di calcolare differenze tra due liste.
     */
    class PrenotazioneDiffCallback : DiffUtil.ItemCallback<PrenotazioneAdmin>() {
        override fun areItemsTheSame(oldItem: PrenotazioneAdmin, newItem: PrenotazioneAdmin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PrenotazioneAdmin, newItem: PrenotazioneAdmin): Boolean {
            return oldItem == newItem
        }
    }
}