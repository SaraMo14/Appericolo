package com.example.appericolo.ui.preferiti.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
      fun insert(contact : Contact)

    @Delete
      fun delete(contact: Contact)

    @Query("SELECT * FROM contacts_table")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("DELETE FROM contacts_table")
    fun deleteAll()

}