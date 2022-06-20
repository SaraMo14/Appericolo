package com.example.appericolo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appericolo.authentication.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginUITest {

        @get : Rule
        var mActivityRule = ActivityScenarioRule(LoginActivity::class.java)
        private lateinit var email: String
        private lateinit var password: String
        @Before
        fun setUp() {
                email ="miaooo@gmail.com"
                password =  "Piopio00@"
        }

        /**
         * Testare se il login funziona, ovvero se inserendo le credenziali e premendo
         * il bottone, si naviga verso la Main Activity
         */
        @Test
        fun loginTest() {
                onView(withId(com.example.appericolo.R.id.login_email)).perform(ViewActions.replaceText(email), closeSoftKeyboard());
                onView(withId(com.example.appericolo.R.id.login_password)).perform(ViewActions.replaceText(password), closeSoftKeyboard());

                onView(withId(com.example.appericolo.R.id.login_button)).perform(click())
                //pausa utile per far caricare la main activity
                Thread.sleep(3000)
                onView(withId(com.example.appericolo.R.id.container_main_activity)).check(matches(isDisplayed()))
        }



        @After
        fun tearDown() {
            //clean up code
        }

}