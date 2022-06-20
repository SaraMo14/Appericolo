package com.example.appericolo

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.example.appericolo.ui.preferiti.contacts.database.ContactDao
import com.example.appericolo.ui.preferiti.contacts.database.ContactRoomDatabase
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//classe di test per l'inserimento di contatti nel db Room

@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
class RoomTest : TestCase() {
    // get reference to the LanguageDatabase and LanguageDao class
    private lateinit var db: ContactRoomDatabase
    private lateinit var dao: ContactDao
    private lateinit var contact: Contact

    @get:Rule
    //We are using InstantTaskExecutorRule(), A JUnit Test Rule that swaps the background executor used by the
    //Architecture Components with a different one that executes each task synchronously.
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, ContactRoomDatabase::class.java).build()
        dao = db.contactDao()
        contact = Contact("38984023872", "Mario Rossi")
    }


     @After
    fun closeDb() {
        dao.delete(contact)
        db.close()
    }

    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }
    @Test
    fun writeAndReadContact()  {
        dao.getAllContacts().observeOnce {
            assertEquals(0, it.size)
        }
        //val contact = Contact("38984023872", "Mario Rossi")
        dao.insert(contact)

        dao.getAllContacts().observeOnce {
            assertEquals(1, it.size)
        }
    }

}