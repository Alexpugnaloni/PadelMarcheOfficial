package com.example.padelmarcheofficial.ui.prenotazioni

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.padelmarcheofficial.dataclass.GestioneAccount

class PrenotaUnaPartitaViewModel : ViewModel() {

    private val _listasedi = MutableLiveData<List<String>>().apply {
        value = listOf("")
    }
    val listasedi: LiveData<List<String>> = _listasedi

    suspend fun init(){
        _listasedi.postValue(GestioneAccount().downloadNomiSedi())
    }
}