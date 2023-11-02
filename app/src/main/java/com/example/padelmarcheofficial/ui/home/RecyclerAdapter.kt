package com.example.padelmarcheofficial.ui.search

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.padelmarcheofficial.R

/**
 * * Classe per la gestione del RecyclerView nell'*SearchFragment.kt*.
 *
 * @author
 */
class RecyclerAdapter(private var elenco: MutableList<MutableMap<String,Any>>,private val listener: ViewHolder.ClickListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    /**
     * ViewHolder che gestisce il singolo elemento ottenuto dalla ricerca
     */
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val textname : TextView = itemview.findViewById(R.id.row_data)
        var id : String = ""

        interface ClickListener {
            fun onClickListener(id:String,email:String)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Si fa l'inflate del layout per ogni elemento della recyclerview
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.prenotazioni_row,parent, false)
        return ViewHolder(layout)
    }

    /**
     * Assegna a ogni view i valori di ogni campo: nome, immagine
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id=elenco[position].get("id").toString()
        holder.textname.text= elenco[position].get("nome").toString()
        holder.itemView.setOnClickListener {
            listener.onClickListener(elenco[position].get("id").toString(),"s${elenco[position].get("email").toString()}")
        }
    }

    /**
     * restituisce il numero di elementi dell'array
     *
     * @return elenco.size
     */
    override fun getItemCount(): Int {
        return elenco.size
    }
}
