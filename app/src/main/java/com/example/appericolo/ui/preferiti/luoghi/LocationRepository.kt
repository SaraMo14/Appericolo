package com.example.appericolo.ui.preferiti.luoghi

import androidx.annotation.WorkerThread
import com.example.appericolo.ui.preferiti.luoghi.database.Location
import com.example.appericolo.ui.preferiti.luoghi.database.LocationDao
import com.example.appericolo.ui.preferiti.luoghi.database.LocationFirebase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class LocationRepository(private val locationDao: LocationDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allLocations: Flow<List<Location>> = locationDao.getAll()
    val locationFirebase= LocationFirebase()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(location: Location) {
        locationDao.insert(location)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(location: Location) {
        locationDao.delete(location)
    }

    fun insertCurrentLocation(position: LatLng) {
        locationFirebase.insertCurrentLocation(position)
    }

    suspend fun deleteAllLocations() {
        locationDao.deleteAll()
    }
}