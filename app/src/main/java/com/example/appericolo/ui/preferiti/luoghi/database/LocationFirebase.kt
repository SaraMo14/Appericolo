package com.example.appericolo.ui.preferiti.luoghi.database

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

class LocationFirebase {
    var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/" + Firebase.auth.currentUser?.uid.toString())

    //metodo per aggiornare la posizione di un utente
    fun insertCurrentLocation(position: LatLng){
        database.child("position").setValue(position) //contiene position.latitude e position.longitude
    }

}