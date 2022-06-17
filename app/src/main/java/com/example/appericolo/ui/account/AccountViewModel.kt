package com.example.appericolo.ui.account

import android.app.Application
import android.app.Service
import androidx.lifecycle.*
import com.example.appericolo.authentication.User
import com.example.appericolo.ui.preferiti.contacts.ContactRepository
import com.example.appericolo.ui.preferiti.contacts.database.ContactRoomDatabase


class AccountViewModel(application: Application): AndroidViewModel(application) {

    val repository : ContactRepository

    val user: User



    init {
        repository = ContactRepository(ContactRoomDatabase.getDatabase(application))
        user = repository.user
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    val text: LiveData<String> = _text
}
