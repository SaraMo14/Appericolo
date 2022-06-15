package com.example.appericolo.ui.preferiti.contacts.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase



class ContactsFirebase {
    var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/" + Firebase.auth.currentUser?.uid.toString() + "/favContacts")


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
    /*fun writeToRemoteDb(contact: Contact) {
        val contactListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(contact.name)) {
                    database.child(contact.name).setValue(contact.number)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //vorrei mettere un toast per dire che il contatto già c'è ma non riesco
            }
        }
        database.addValueEventListener(contactListener)
        //database.child(contact.name).setValue(contact.number)
    }

     */

    fun deleteFromRemoteDb(contact: Contact) {
        database.child(contact.name).removeValue()
    }

}