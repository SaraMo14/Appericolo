package com.example.appericolo.ui.preferiti.luoghi

import android.app.Application
import com.example.appericolo.ui.preferiti.luoghi.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LocationApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { LocationRepository(database.locationDao()) }
}