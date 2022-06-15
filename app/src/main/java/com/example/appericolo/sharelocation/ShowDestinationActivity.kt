package com.example.appericolo.sharelocation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.appericolo.R
import com.example.appericolo.databinding.ActivityShowDestinationBinding
import com.example.appericolo.ui.map.MapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class ShowDestinationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var destination: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityShowDestinationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        destination= intent.getStringExtra("indirizzo").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.backwardFloatingActionButton.setOnClickListener{
            finish()
        }
        binding.forwardFab.setOnClickListener{
            val address = Geocoder(this).getFromLocationName(destination, 1)[0]
            val sendIntent = Intent(this, ArrivalTimeActivity::class.java)
            val args = Bundle()
            args.putParcelable("destinazione", LatLng(address.latitude, address.longitude))
            //args.putParcelable("partenza", LatLng(MapFragment.lastLocation.latitude, MapFragment.lastLocation.longitude ) )
            sendIntent.putExtra("LocationsBundle", args)
            //startActivityForResult(sendIntent, 400)
            startActivity(sendIntent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Activity.RESULT_OK && requestCode == 400) {
            //setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        showCurrentLocation()
        showDestination()
    }

    private fun showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapFragment.LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
            if(it != null){

                MapFragment.lastLocation = it
                val currentLatLng = LatLng(it.latitude, it.longitude)
                placeMarkerOnMap(currentLatLng, mMap, R.drawable.clipart1828626)
            }
        }

    }

    private fun showDestination(){
        var addressList: List<Address>? = null
        val geoCoder = Geocoder(this)
        try {
            addressList = geoCoder.getFromLocationName(destination, 1)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        try{
            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)

            placeMarkerOnMap(latLng, mMap)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }catch (e: Exception)
        {
            Toast.makeText(this, "Indirizzo non trovato. Tornare indietro e inserire un indirizzo valido", Toast.LENGTH_LONG).show()
        }

    }
    private fun placeMarkerOnMap(currentLatLng: LatLng, mMap: GoogleMap, markerIconId: Int = 0 ) {



        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        if(markerIconId!=0){
            //make function
            var height = 70
            var width = 70
            var bitmapdraw = resources.getDrawable(markerIconId) as BitmapDrawable
            var b = bitmapdraw.bitmap
            var smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        }
        mMap.addMarker(markerOptions)
    }



}
