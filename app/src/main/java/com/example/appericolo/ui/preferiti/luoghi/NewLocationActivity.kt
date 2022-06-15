package com.example.appericolo.ui.preferiti.luoghi

import android.location.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.os.Build
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.appericolo.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import java.io.IOException


class NewLocationActivity() : FragmentActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mMap: GoogleMap? = null
    internal lateinit var address: Address
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val button = findViewById<Button>(R.id.save_location_button)
        button.setOnClickListener {
            if (TextUtils.isEmpty(findViewById<EditText>(R.id.edit_indirizzo).text)) {
                Toast.makeText(applicationContext,"Inserire luogo",Toast.LENGTH_SHORT).show()
            //setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_REPLY, 1)
                replyIntent.putExtra("indirizzo",
                    findViewById<EditText>(R.id.edit_indirizzo).text.toString())
                replyIntent.putExtra("latitudine", address.latitude.toString())
                replyIntent.putExtra("longitudine", address.longitude.toString())
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient()
                mMap!!.isMyLocationEnabled = true
            }
        } else {
            buildGoogleApiClient()
            mMap!!.isMyLocationEnabled = true
        }

    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    override fun onConnected(bundle: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(i: Int) {

    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

     fun searchLocation(view: View) {
        val locationSearch:EditText = findViewById<EditText>(R.id.edit_indirizzo)
        var location: String = locationSearch.text.toString()
        var addressList: List<Address>? = null

        if (location == null || location == "") {
            Toast.makeText(applicationContext,"Inserire luogo",Toast.LENGTH_SHORT).show()
        }
        else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)

            mMap!!.clear()
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            //mMap!!.animateCamera(CameraUpdateFactory.zoomTo(6f))
            Toast.makeText(this.applicationContext, address.latitude.toString() + " " + address.longitude, Toast.LENGTH_LONG).show()

        }
    }

    override fun onLocationChanged(p0: android.location.Location) {
        TODO("Not yet implemented")
    }
}