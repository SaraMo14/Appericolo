package com.example.appericolo.sharelocation

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appericolo.R
import com.example.appericolo.databinding.ActivityLocationUpdatesReceiverBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class LocationUpdatesReceiverActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationUpdatesReceiverBinding
    private lateinit var destination: LatLng
    private lateinit var currentPosition: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationUpdatesReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val senderUid = intent.getStringExtra("senderUid")
        val stopSharingFlag = intent.getStringExtra("stopSharing")
        val arrivalTime = intent.getStringExtra("arrivalTime").toString()
        val destinationLat = intent.getStringExtra("destinationLat")?.toDouble()
        val destinationLong = intent.getStringExtra("destinationLong")?.toDouble()
        destination = LatLng(destinationLat!!, destinationLong!!)

        var previousLocationMarker: Marker? = null



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val fireBase = FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/").getReference(
            "users/" + senderUid+ "/position")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    //for (ds in dataSnapshot.children) {
                    val lat = dataSnapshot.child("latitude").getValue(Double::class.java)
                    val long = dataSnapshot.child("longitude").getValue(Double::class.java)
                    //currentPosition = LatLng(lat!!, long!!)
                    Log.d("location", lat.toString())
                    Log.d("location", long.toString())
                    binding.textView3.text = lat.toString() + " " + long.toString()
                    currentPosition = LatLng(lat!!, long!!)

                    previousLocationMarker = placeMarkerOnMap(currentPosition, mMap, R.drawable.ic_baseline_directions_walk_24)
                    /*try{
                        previousLocationMarker?.remove()
                        previousLocationMarker = placeMarkerOnMap(currentPosition, mMap)
                    }catch(e:Exception){
                        Log.d("location", e.toString())
                    }*/

                    //se il tempo di arrivo previsto è passato e il client non è nelle vicinanze (200 metri)
                    //della destinazione, allora allerta gli amici con cui sta condividendo
                    if(!isCloseToDestination(currentPosition,destination) && isArrivalTimeExpired(arrivalTime)){
                        showDialog("L'orario di arrivo stimato è passato e l'utente è lontano dalla destinazione. Verifica che sia al sicuro!", "Safe arrival")
                    }

                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("Data", databaseError.getMessage())
            }


        }
        fireBase.addValueEventListener(valueEventListener)

        //se il client ha interrotto la condivisione della posizione
        if(stopSharingFlag == "true"){
            showDialog("L'utente è arrivato a destinazione sano e salvo!", "Safe arrival")
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(destination).title("Destinazione"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination))

    }




    private fun showDialog(message: String, title:String){
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
            builder.setMessage(message)

            builder.apply {
                setPositiveButton("Chiudi",
                    DialogInterface.OnClickListener { dialog, id ->
                        //finish()
                    })
            }

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }



    //ritorna true se il tempo di arrivo previsto è passato, false se non è ancora arrivato

    companion object{


          fun isArrivalTimeExpired(time: String): Boolean{

            val sdf = SimpleDateFormat("HH:mm")
            val time: Date = sdf.parse(time)

            val timeToMatch = Calendar.getInstance()
            timeToMatch[Calendar.HOUR_OF_DAY] = time.hours
            timeToMatch[Calendar.MINUTE] = time.minutes
            val currentTime = Calendar.getInstance()

            when {
                currentTime == timeToMatch -> return true// the times are equals
                currentTime < timeToMatch -> return false// currentTime is before timeToMatch
                currentTime > timeToMatch -> return true// currentTime is after timeToMatch
            }
            return false
        }

        //ritorna true se il client è vicino alla destinazione, false se è lontano
         fun isCloseToDestination(currentPosition: LatLng, destination: LatLng?): Boolean {
            val distance = FloatArray(1)
            Location.distanceBetween(destination!!.latitude, destination!!.longitude,
                currentPosition.latitude, currentPosition.longitude, distance)
            val radiusInMeters = 150.0 //150 Metri
            if( distance[0] > radiusInMeters ){
                //Toast.makeText(this.applicationContext,
                    //"Outside, distance from center: " + distance[0] + " radius: " + radiusInMeters,
                    //Toast.LENGTH_LONG).show()
                return false
            } else {
               // Toast.makeText(this.applicationContext,
                    //"Inside, distance from center: " + distance[0] + " radius: " + radiusInMeters ,
                    //Toast.LENGTH_LONG).show()
                return true
            }
        }
    }
    private fun placeMarkerOnMap(currentLatLng: LatLng, mMap: GoogleMap, markerIconId: Int=0): Marker {

        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        if(markerIconId!=0){
            //make function
            val height = 70
            val width = 70
            val bitmapdraw = resources.getDrawable(markerIconId) as BitmapDrawable
            val b = bitmapdraw.bitmap
            val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        }
        return mMap.addMarker(markerOptions)!!
    }
}
