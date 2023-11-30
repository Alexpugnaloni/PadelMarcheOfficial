import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R
import com.example.padelmarcheofficial.dataclass.CentroSportivo
import com.example.padelmarcheofficial.dataclass.Prenotazione
import com.example.padelmarcheofficial.dataclass.UserValue

//package com.example.padelmarcheofficial.ui.prenotazioni

class PrenotazioniAdapter : ListAdapter<Prenotazione, PrenotazioniAdapter.PrenotazioneViewHolder>(PrenotazioneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrenotazioneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return PrenotazioneViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrenotazioneViewHolder, position: Int) {
        val currentPrenotazione = getItem(position)
        holder.bind(currentPrenotazione)
    }

    inner class PrenotazioneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prenotazioneUtenteTextView: TextView = itemView.findViewById(R.id.prenotazioneUtenteTextView)
        private val prenotazioneCSportivoTextView: TextView = itemView.findViewById(R.id.prenotazioneCSportivoTextView)
        private val prenotazioneDataTextView: TextView = itemView.findViewById(R.id.prenotazioneDataTextView)

        fun bind(prenotazione: Prenotazione) {
            val datiUtente = UserValue()
            if (prenotazione.utente == datiUtente.getId().toString()) {
                prenotazioneUtenteTextView.text = datiUtente.getNome() + " " + datiUtente.getCognome()
            }
            prenotazioneCSportivoTextView.text = prenotazione.centroSportivo
            prenotazioneDataTextView.text = prenotazione.date.toString()

        }
    }

    class PrenotazioneDiffCallback : DiffUtil.ItemCallback<Prenotazione>() {
        override fun areItemsTheSame(oldItem: Prenotazione, newItem: Prenotazione): Boolean {
            return oldItem.id == newItem.id // Modifica questo controllo se hai un identificatore diverso per le prenotazioni
        }

        override fun areContentsTheSame(oldItem: Prenotazione, newItem: Prenotazione): Boolean {
            return oldItem == newItem // Controlla se i contenuti sono gli stessi, nel caso delle prenotazioni, è bene controllare l'uguaglianza
        }
    }
}