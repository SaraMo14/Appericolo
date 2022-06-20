package com.example.appericolo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appericolo.authentication.LoginActivity
import com.example.appericolo.authentication.RegisterActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegistrationUITest {

        @get : Rule
        var mActivityRule = ActivityScenarioRule(RegisterActivity::class.java)
        private lateinit var email: String
        private lateinit var password: String
        @Before
        fun setUp() {
                email ="miaooo@gmail.com"
                password =  "Piopio00@"
        }


        /**
         * Testare se il controllo sui campi dedlla registrazione funziona, ovvero se inserendo alcuni dati (non tutti) e premendo
         * il bottone, appare un dialog di errore di invalidità del form
         */
        @Test
        fun registrationTest() {
                onView(withId(com.example.appericolo.R.id.registerEmail)).perform(ViewActions.replaceText(email), closeSoftKeyboard());
                onView(withId(com.example.appericolo.R.id.registerPassword)).perform(ViewActions.replaceText(password), closeSoftKeyboard());

                onView(withId(com.example.appericolo.R.id.registerButton)).perform(click())
                //pausa utile per far caricare la main activity
                Thread.sleep(3000)
                onView(withText("Invalid Form")).check(matches(isDisplayed()))
        }

        @After
        fun tearDown() {
            //clean up code
        }

}