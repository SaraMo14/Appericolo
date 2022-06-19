package com.example.appericolo

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.test.runBlockingTest
import com.example.appericolo.ui.preferiti.luoghi.data.AppDatabase
import com.example.appericolo.ui.preferiti.luoghi.data.Location
import com.example.appericolo.ui.preferiti.luoghi.data.LocationDao
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LoadLocationTest {
    // A JUnit Test Rule that swaps the background executor
// used by the Architecture Components with a different one          //which executes each task synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

        private lateinit var locationDao: LocationDao
        private lateinit var db: AppDatabase

        @Before
        fun createDb() {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
            locationDao = db.locationDao()
        }

        @After
        @Throws(IOException::class)
        fun closeDb() {
            db.close()
        }

    /*
    test case to insert location in room database
    */
    @Test
        fun writeLocationAndReadInList() =  runBlocking {
            val location: Location = Location(0, "randomLocation", "43.0", "43.0")
            locationDao.insert(location)
            val locations = locationDao.getAll().asLiveData().value
            //val locList: List<Location> = locations.fla
            assertTrue(locations!!.contains(location))
        }
}