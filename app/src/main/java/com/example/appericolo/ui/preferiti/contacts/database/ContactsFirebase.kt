package com.example  .appericolo.ui.preferiti.contacts.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appericolo.authentication.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread


class ContactsFirebase {


    var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/" + Firebase.auth.currentUser?.uid.toString() + "/favContacts")

    var database2: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/")

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

    fun getUserLoggedInfo(): User {
        val user = User()
        database2.child(Firebase.auth.currentUser?.uid.toString()).get().addOnSuccessListener { infos->

            user.name = infos.child("name").getValue().toString()
            user.surname = infos.child("surname").getValue().toString()
            user.email = infos.child("email").getValue().toString()
            user.cell_number = infos.child("cell_number").getValue().toString()
        }.addOnFailureListener{
        }
        Thread.sleep(2000)
        return user
    }
     /*fun getUserLoggedInfo(myCallback: UserCallbck){
        database2.child(Firebase.auth.currentUser?.uid.toString())addOnSuccessListener { userLogged->
            name = userLogged.child("name").getValue().toString()
            surname  = userLogged.child("surname").getValue().toString()
            email   = userLogged.child("email").getValue().toString()
            cell_number  = userLogged.child("cell_number").getValue().toString()

            Log.i("userlogged name1 ",name )
        }
         Log.i("userlogged name2 ",name )

         //var user = User(name,surname,cell_number,email)
         //Log.i("userlogged1", user.name.toString() )
         Thread.sleep(2000)
         Log.i("userlogged name3 ",name )



         return User(name,surname,cell_number,email)
    }
*/

    fun getFavContactsTokens(favContacts: ArrayList<Contact>): ArrayList<String>{
        var res = ArrayList<String>()

        runBlocking {
            var tokens = ArrayList<String>()
            database.parent?.parent?.get()?.addOnSuccessListener { users->
            var job1 = this.async{
                for (user in users.children){
                    for (favContact in favContacts) {
                        if (favContact.number == user.child("cell_number").getValue().toString() ||
                            favContact.number == "+39" + user.child("cell_number").getValue().toString() ||
                            "+39" + favContact.number == user.child("cell_number").getValue().toString() ) {
                            tokens.add(user.child("token").getValue().toString())

                        }
                    }
                }

            }
            //Thread.sleep(3000)
            //Log.i("debug", tokens.size.toString())
        }

           tokens}

        return res
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
