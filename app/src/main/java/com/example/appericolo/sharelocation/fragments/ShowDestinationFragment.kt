package com.example.appericolo.sharelocation.fragments

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.appericolo.R
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class ShowDestinationFragment : Fragment() {


    private lateinit var mMap: GoogleMap
    private lateinit var destination: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        showCurrentLocation()
        showDestination()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view=  inflater.inflate(R.layout.fragment_show_destination, container, false)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())

        destination = arguments?.getString("indirizzo").toString()
        view.findViewById<FloatingActionButton>(R.id.forwardFab).setOnClickListener{
            val address = Geocoder(this.requireContext()).getFromLocationName(destination, 1)[0]
            val bundle = bundleOf("coordinate" to LatLng(address.latitude, address.longitude), "indirizzo" to destination)
            Navigation.findNavController(view).navigate(R.id.action_showDestinationFragment_to_arrivalTimeFragment, bundle)

        }

        return view
    }



    private fun showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapFragment.LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) {
            if(it != null){

                MapFragment.lastLocation = it
                val currentLatLng = LatLng(it.latitude, it.longitude)
                placeMarkerOnMap(currentLatLng, mMap, R.drawable.clipart1828626)
            }
        }

    }


    private fun showDestination(){
        var addressList: List<Address>? = null
        val geoCoder = Geocoder(this.requireContext())
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
            Toast.makeText(this.requireContext(), "Indirizzo non trovato. Tornare indietro e inserire un indirizzo valido", Toast.LENGTH_LONG).show()
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