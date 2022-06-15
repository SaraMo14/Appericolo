package com.example.appericolo.authentication

import com.example.appericolo.ui.preferiti.contacts.database.Contact

data class User(val name: String, val surname: String, val cell_number: String, val email:String, val favContacts: ArrayList<Contact>)
