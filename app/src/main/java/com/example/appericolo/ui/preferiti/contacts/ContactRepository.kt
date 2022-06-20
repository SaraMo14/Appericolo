package com.example.appericolo.ui.preferiti.contacts

import androidx.lifecycle.LiveData
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.example.appericolo.ui.preferiti.contacts.database.ContactRoomDatabase
import com.example.appericolo.ui.preferiti.contacts.database.ContactsFirebase

class ContactRepository(private val database : ContactRoomDatabase) {

    var contactsFirebase = ContactsFirebase()
    var allFavContacts: LiveData<List<Contact>> = database.contactDao().getAllContacts()

     fun getTokens(): ArrayList<String>{
        //val result = contactsFirebase.getFavContactsTokens(allFavContacts.value as ArrayList<Contact>)
        contactsFirebase.getFavContactsTokens(allFavContacts.value as ArrayList<Contact>)
            //Log.i("debug", result.size.toString() )
        return contactsFirebase.tokens
    }

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

    fun retrieveAndStoreToken(){
        contactsFirebase.retrieveAndStoreToken()
    }

    fun clearToken(userUid: String){
        contactsFirebase.clearToken(userUid)
    }
}