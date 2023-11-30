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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
      //  private val prenotazioneUtenteTextView: TextView = itemView.findViewById(R.id.prenotazioneUtenteTextView)
        private val prenotazioneCSportivoTextView: TextView = itemView.findViewById(R.id.prenotazioneCSportivoTextView)
        private val prenotazioneDataTextView: TextView = itemView.findViewById(R.id.prenotazioneDataTextView)
        private val prenotazionePartecipantiTextView: TextView = itemView.findViewById(R.id.prenotazionePartecipantiTextView)


        fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = Date(timestamp)
            return sdf.format(date)
        }
        fun bind(prenotazione: Prenotazione) {
            val datiUtente = UserValue()

            val mappaSedi = mapOf(
                "EHRgWL3MllLaSuWPzddd" to "Ascoli Piceno",
                "VapW6qTS1HOHEmORefB5" to "Pesaro Urbino",
                "gPNgSKQgkrg8G0yXmWV4" to "Macerata",
                "ge42drMdBlsI3MUuVeuX" to "Ancona",
                "ixvptNZVldQwyKAMTYkD" to "Fermo"
            )

            val nomeSede = mappaSedi[prenotazione.centroSportivo]

        /*    if (prenotazione.utente == datiUtente.getId().toString()) {
                prenotazioneUtenteTextView.text = "Nome e Cognome: " +  datiUtente.getNome() + " " + datiUtente.getCognome()
            }*/
            if (nomeSede != null){
                prenotazioneCSportivoTextView.text = "Sede: " + nomeSede
            }

            val timestamp = prenotazione.date.time
            val formattedDate = formatTimestamp(timestamp)
            prenotazioneDataTextView.text = "Giorno e ora: " + formattedDate

            //VERIFICARE INCONGRUENZA DELLO 0 LISTAUTENTI CHE è 3 SE PRENOTO TUTTO IL CAMPO E 0 SE HO FATTO UNISCITI
            val partecipanti: Int = when (prenotazione.listautenti.size) {
                0 -> 3
                1 -> 1
                2 -> 2
                3, 4 -> 3
                else -> prenotazione.listautenti.size // Valore predefinito se non rientra in nessuna condizione
            }

            prenotazionePartecipantiTextView.text = "Altri Partecipanti: $partecipanti"
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