package com.example.appericolo.ui.preferiti.contacts

import android.app.Application
import androidx.lifecycle.*
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.example.appericolo.ui.preferiti.contacts.database.ContactRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Contact>>
    val readAllDataFromLocal: LiveData<List<Contact>>
    private val repository: ContactRepository


    init {
        repository = ContactRepository(ContactRoomDatabase.getDatabase(application))
        readAllData = repository.allFavContacts
        readAllDataFromLocal = repository.allFavContacts
        }

    fun putFavFromRemoteToLocal(){
        viewModelScope.launch(Dispatchers.IO){
            repository.putFavFromRemoteToLocal()
        }
    }


    fun addContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(contact)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    /*fun getFavContacts(){
    viewModelScope.launch(Dispatchers.IO){
        repository.getFavContacts()
    }
}

 */

    fun removeContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(contact)
        }
    }
}
