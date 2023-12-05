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
import com.example.padelmarcheofficial.dataclass.CentroSportivo
import com.example.padelmarcheofficial.dataclass.GestioneFirebase
import com.example.padelmarcheofficial.dataclass.Prenotazione
import com.example.padelmarcheofficial.dataclass.UserValue
import com.example.padelmarcheofficial.ui.prenotazioni.PrenotazioneClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//package com.example.padelmarcheofficial.ui.prenotazioni

class PrenotazioniAdapter(private val prenotazioneClickListener: PrenotazioneClickListener) : ListAdapter<Prenotazione, PrenotazioniAdapter.PrenotazioneViewHolder>(PrenotazioneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrenotazioneViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return PrenotazioneViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrenotazioneViewHolder, position: Int) {
        val currentPrenotazione = getItem(position)
        holder.bind(currentPrenotazione)
    }

    inner class PrenotazioneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val prenotazioneCSportivoTextView: TextView = itemView.findViewById(R.id.prenotazioneCSportivoTextView)
        private val prenotazioneDataTextView: TextView = itemView.findViewById(R.id.prenotazioneDataTextView)
        private val prenotazionePartecipantiTextView: TextView = itemView.findViewById(R.id.prenotazionePartecipantiTextView)


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
            // ... il resto del codice del ViewHolder


        fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = Date(timestamp)
            return sdf.format(date)
        }
        fun bind(prenotazione: Prenotazione) {


            val mappaSedi = mapOf(
                "EHRgWL3MllLaSuWPzddd" to "Ascoli Piceno",
                "VapW6qTS1HOHEmORefB5" to "Pesaro Urbino",
                "gPNgSKQgkrg8G0yXmWV4" to "Macerata",
                "ge42drMdBlsI3MUuVeuX" to "Ancona",
                "ixvptNZVldQwyKAMTYkD" to "Fermo"
            )

            val nomeSede = mappaSedi[prenotazione.centroSportivo]


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
                else -> prenotazione.listautenti.size
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