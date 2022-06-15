package com.example.appericolo.sharelocation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appericolo.databinding.ActivityLocationUpdatesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LocationUpdatesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationUpdatesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationUpdatesBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val senderUid = intent.getStringExtra("senderUid")
        val fireBase = FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/").getReference(
            "users/" + senderUid+ "/position")
        Log.i("LocUpdates", senderUid.toString())
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot!!.exists()){
                    //for (ds in dataSnapshot.children) {
                        val lat = dataSnapshot.child("latitude").getValue(Double::class.java)
                        val long = dataSnapshot.child("longitude").getValue(Double::class.java)
                        Log.d("location", lat.toString())
                        Log.d("location", long.toString())
                    binding.textView3.text = lat.toString() + " " + long.toString()
                    //}
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Data", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        fireBase.addValueEventListener(valueEventListener)
        //binding.textView3.text =

    }
}