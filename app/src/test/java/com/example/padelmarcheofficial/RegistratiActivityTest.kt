package com.example.padelmarcheofficial

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RegistratiActivityTest {
    @Test
    fun verificaInserimento1() {
        //I dati del profilo del creatore Alex sono validi, risultano corretti e conformi
        assertEquals(true,RegistratiViewModel().verificaInserimento(
            nome="Alex",
            cognome="Pugnaloni",
            email="alexpugnaloni@gmail.com",
            psw= "passwordFinta",
            compleanno="08-06-2000",
            cellulare = "3387826790"
        ))
    }
    @Test
    fun verificaInserimento2() {
        //I dati del profilo del creatore Elia sono validi, risultano corretti e conformi
        assertEquals(true,RegistratiViewModel().verificaInserimento(
            nome="Elia",
            cognome="Vaccarini",
            email="eliavaccarini@gmail.com",
            psw= "p1s2r8j4",
            compleanno="20-05-1999",
            cellulare = "3923850157"
        ))
    }

    @Test
    fun verificaInserimento3() {
        //Il cellulare non è corretto
        assertEquals(false,RegistratiViewModel().verificaInserimento(
            nome="Alex",
            cognome="Pugnaloni",
            email="alexpugnaloni@gmail.com",
            psw= "passwordFinta",
            compleanno="08-06-2000",
            cellulare = "333"
        ))
    }
    @Test
    fun verificaInserimento4() {
        //L'email non rispetta i requisiti
        assertEquals(false,RegistratiViewModel().verificaInserimento(
            nome="Elia",
            cognome="Vaccarini",
            email="eliavaccarini.it",
            psw= "p1s2r8j4",
            compleanno="20-05-1999",
            cellulare = "3923850157"

        ))
    }
    @Test
    fun verificaInserimento5() {
        //la password non mostra almeno 8 caratteri
        assertEquals(false,RegistratiViewModel().verificaInserimento(
            nome="Alex",
            cognome="Pugnaloni",
            email="alexpugnaloni@gmail.com",
            psw= "Caio",
            compleanno="08-06-2000",
            cellulare = "3387826790"

        ))
    }
    @Test
    fun verificaInserimento6() {
        //Il nome è una stringa vuota
        assertEquals(false,RegistratiViewModel().verificaInserimento(
            nome="",
            cognome="Cognome",
            email="alexpugnaloni@gmail.com",
            psw= "esempio12",
            compleanno="08-06-2000",
            cellulare = "3387826790"
        ))
    }
}