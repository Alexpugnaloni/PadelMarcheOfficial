package com.example.padelmarcheofficial.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.dataclass.Prenotazione
import com.example.padelmarcheofficial.dataclass.PrenotazioneAdmin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PrenotazioniAdminAdapter : ListAdapter<PrenotazioneAdmin, PrenotazioniAdminAdapter.PrenotazioneViewHolder>(PrenotazioneDiffCallback()) {

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


        fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = Date(timestamp)
            return sdf.format(date)
        }
        fun bind(prenotazione: PrenotazioneAdmin) {



            val timestamp = prenotazione.date.time
            val formattedDate = formatTimestamp(timestamp)
            prenotazioneDataTextView.text = "Giorno e ora: " + formattedDate

            //VERIFICARE INCONGRUENZA DELLO 0 LISTAUTENTI CHE è 3 SE PRENOTO TUTTO IL CAMPO E 0 SE HO FATTO UNISCITI
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

    class PrenotazioneDiffCallback : DiffUtil.ItemCallback<PrenotazioneAdmin>() {
        override fun areItemsTheSame(oldItem: PrenotazioneAdmin, newItem: PrenotazioneAdmin): Boolean {
            return oldItem.id == newItem.id // Modifica questo controllo se hai un identificatore diverso per le prenotazioni
        }

        override fun areContentsTheSame(oldItem: PrenotazioneAdmin, newItem: PrenotazioneAdmin): Boolean {
            return oldItem == newItem // Controlla se i contenuti sono gli stessi, nel caso delle prenotazioni, è bene controllare l'uguaglianza
        }
    }
}