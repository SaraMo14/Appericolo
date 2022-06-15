package com.example.appericolo.ui.preferiti.contacts.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//entities=arrayOf(Contact::class)
@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactRoomDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object{
        private var INSTANCE: ContactRoomDatabase? =  null

        fun getDatabase(context: Context): ContactRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactRoomDatabase::class.java,
                    "contact_database"
                ).build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}