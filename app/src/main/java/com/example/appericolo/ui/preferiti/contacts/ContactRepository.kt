package com.example.appericolo.ui.preferiti.contacts

import androidx.lifecycle.LiveData
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.example.appericolo.ui.preferiti.contacts.database.ContactsFirebase
import com.example.appericolo.ui.preferiti.contacts.database.ContactRoomDatabase

class ContactRepository(private val database : ContactRoomDatabase) {


    var contactsFirebase = ContactsFirebase()
    var allFavContacts: LiveData<List<Contact>> = database.contactDao().getAllContacts()


    /*suspend fun getFavContacts(){
        var list: ArrayList<Contact> = contactsFirebase.getFavContacts()!!
        for (con in list){
            contactDao.insert(con)
        }
    }

     */
     fun putFavFromRemoteToLocal(){
        var list = contactsFirebase.getFavContacts()

        for (contact in list){
            database.contactDao().insert(contact)
        }
    }


     fun insert(contact: Contact) {
        contactsFirebase.writeToRemoteDb(contact)
        database.contactDao().insert(contact)
    }

     fun delete(contact: Contact){
        contactsFirebase.deleteFromRemoteDb(contact)
        database.contactDao().delete(contact)
    }

     fun deleteAll(){
        database.contactDao().deleteAll()
    }
}