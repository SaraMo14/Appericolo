package com.example.appericolo.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.appericolo.fakecall.InfoFakeCallActivity
import com.example.appericolo.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.material.floatingactionbutton.FloatingActionButton

//Classe che mostra la mappa con la posizione attuale dell'utente nella home
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
ActivityCompat.OnRequestPermissionsResultCallback {

    //MAP SETTINGS
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        lateinit var lastLocation: Location
        const val LOCATION_PERMISSION_REQUEST_CODE = 1

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        //Custom Theme
        //val style = MapStyleOptions.loadRawResourceStyle(this.requireContext(), R.raw.home_map_style)
        //mMap.setMapStyle(style)
        mMap.setOnMarkerClickListener(this)
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.requireContext(), R.raw.home_map_style))
            if (!success) {
                Log.e("style", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("style", "Can't find style.", e)
        }
        fetchLocation()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_map_home, container, false)


        view.findViewById<FloatingActionButton>(R.id.goToFakeCallButton).setOnClickListener() {
            val intent = Intent(this.requireContext(), InfoFakeCallActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<FloatingActionButton>(R.id.goToSelectDestinationButton)
            .setOnClickListener() {
                Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_selectDestinationFragment)

            }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_bottom) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())

        return view
    }


    //metodo che individua la posizione attuale dell'utente
    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        if (checkLocationPermission()) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) {
                if (it != null) {
                    lastLocation = it
                    mMap.clear()
                    }
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        //Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }



    private fun checkLocationPermission(): Boolean {
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
                ActivityCompat.requestPermissions(this.requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
                return false
            }
            GPSstatusCheck()
            return true

        }

        private fun GPSstatusCheck() {
            val manager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            }
        }

        private fun buildAlertMessageNoGps() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
            builder.setMessage("Il tuo GPS ?? disabilitato. Vuoi abilitarlo?")
                .setCancelable(false)
                .setPositiveButton("S??",
                    DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }




    }
