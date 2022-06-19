package com.example.appericolo.utils

import com.example.appericolo.sharelocation.fcm.NotificationAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

object CommonInfo {
    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY="AAAAmU6JhFM:APA91bGP9zbRDKupSPAOnFWE7Bgjpjy8iMbWg47p5YFQltUD1EOVG0s9sfo8YCUo-dqtgwkO9OOZUgylIdF-1A4lG6CoXQNopje9EoeGRnjqD7c81qQcxwI0h67tWRRYv__v5VizfPSA"
    const val CONTENT_TYPE="application/json"

    fun retrieveAndStoreToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(it.isSuccessful){
                val token = it.result
                val userUid= FirebaseAuth.getInstance().currentUser!!.uid
                //tokens/userUid/token
                FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
                    .getReference("users")
                    .child(userUid)
                    .child("token")
                    .setValue(token)
            }
        }
    }

    fun clearToken(userUid: String){
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
            .child(userUid)
            .child("token")
            .removeValue()
    }
}