package com.example.padelmarcheofficial

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.CoroutineContext

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
internal class AccediActivityTest{
    lateinit var accediActivity : ActivityScenario<AccediActivity>
    @Before
    fun setUp(){
        //faccio il logout per essere certo della validità del risultato
        accediActivity = ActivityScenario.launch(AccediActivity::class.java)
        accediActivity.onActivity{
            it.firebaseAuth.signOut()
            it.testing=true
        }
    }
    @Test
    fun login1(){
        //password non corretta
        onView(withId(R.id.editTextTextEmailAddress3)).perform(replaceText("alexpugnaloni@gmail.com"))
        onView(withId(R.id.editTextTextPassword)).perform(replaceText("12345"))
        onView(withId(R.id.button)).perform(click())
        runBlocking {
            val model = Model(coroutineContext)
            model.doWork()
            model
        }
        accediActivity.onActivity{
            assertEquals(it.firebaseAuth.currentUser!=null,false)
        }
    }
    @Test
    fun login2(){
        //email non corretta
        onView(withId(R.id.editTextTextEmailAddress3)).perform(replaceText("test1gmail.com"))
        onView(withId(R.id.editTextTextPassword)).perform(replaceText("asdfghjkl"))
        onView(withId(R.id.button)).perform(click())
        runBlocking {
            val model = Model(coroutineContext)
            model.doWork()
            model
        }
        accediActivity.onActivity{
            assertEquals( it.firebaseAuth.currentUser!=null,false)
        }
    }
}

/**
 * Classe per attivare un delay che permette la registrazione a firebase
 */
class Model(parentContext: CoroutineContext = Dispatchers.Default) {
    private val modelScope = CoroutineScope(parentContext)
    var result = false

    fun doWork() {
        modelScope.launch {
            Thread.sleep(10000)
            result = true
        }
    }
}