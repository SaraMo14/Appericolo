package com.example.appericolo.ui.preferiti.contacts.database

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.appericolo.ui.preferiti.contacts.database.Contact
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 * Classe per la gestione dei contatti stretti associati ad un utente su firebase
 */

class ContactsFirebase {


    var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/" + Firebase.auth.currentUser?.uid.toString() + "/favContacts")

    var tokens= ArrayList<String>()

     fun getFavContacts(): ArrayList<Contact> {
        val favContactList = ArrayList<Contact>()

        database.get().addOnSuccessListener { contacts->
            for (contact in contacts.children){
                val contact_name = contact.key.toString()
                val contact_number = contact.getValue(String()::class.java).toString()
                val contact = Contact(contact_number, contact_name)
                favContactList.add(contact)
            }
        }.addOnFailureListener{
        }
        Thread.sleep(3000)
        return favContactList
    }

    fun writeToRemoteDb(contact: Contact){
       database.child(contact.name).setValue(contact.number)
    }

    /*fun getFavContactsTokens(favContacts: ArrayList<Contact>){
        tokens.clear()
        database.parent?.parent?.get()?.addOnSuccessListener { users->
            for (user in users.children){
                for (favContact in favContacts) {
                    if (favContact.number == user.child("cell_number").getValue().toString() ||
                        favContact.number == "+39" + user.child("cell_number").getValue().toString() ||
                        "+39" + favContact.number == user.child("cell_number").getValue().toString() ) {
                        Log.i("utente buono", user.child("name").getValue().toString())
                        tokens.add(user.child("token").getValue().toString())

                    }
                }
            }
        }
        Thread.sleep(2000)
    }*/

    fun deleteFromRemoteDb(contact: Contact) {
        database.child(contact.name).removeValue()
    }



    //metodo per memorizzare il registration token di un utente su db realtime
    fun retrieveAndStoreToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(it.isSuccessful){
                val token = it.result
                val userUid= FirebaseAuth.getInstance().currentUser!!.uid
                FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users")
                    .child(userUid)
                    .child("token")
                    .setValue(token)
            }
        }
    }
    //metodo per eliminare il registration token di un utente su db realtime
    fun clearToken(userUid: String){
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
            .child(userUid)
            .child("token")
            .removeValue()
    }

}
