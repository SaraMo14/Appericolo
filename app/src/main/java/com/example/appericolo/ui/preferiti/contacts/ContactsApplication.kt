package com.example.appericolo.ui.preferiti.contacts

import android.app.Application
import com.example.appericolo.ui.preferiti.contacts.database.ContactRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ContactsApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { ContactRoomDatabase.getDatabase(this) }
    val repository by lazy { ContactRepository(database) }

}
