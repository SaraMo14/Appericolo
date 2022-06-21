package com.example.appericolo.sharelocation.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.appericolo.utils.OneTimeObserver
import com.example.appericolo.R
import com.example.appericolo.sharelocation.fcm.NotificationData
import com.example.appericolo.sharelocation.fcm.PushNotification
import com.example.appericolo.sharelocation.fcm.RetrofitInstance
import com.example.appericolo.ui.map.MapFragment
import com.example.appericolo.ui.preferiti.contacts.ContactViewModel
import com.example.appericolo.ui.preferiti.luoghi.LocationApplication
import com.example.appericolo.ui.preferiti.luoghi.LocationViewModel
import com.example.appericolo.ui.preferiti.luoghi.LocationViewModelFactory
import com.example.appericolo.utils.MapsUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Classe per il monitoraggio della propria posizione da parte dell'utente che condivide
 */

class LocationUpdatesClientFragment : Fragment(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, MapsUtil {
    //MAP SETTINGS
    private lateinit var mMap: GoogleMap
    private lateinit var coordinate: LatLng
    private lateinit var arrivalTime: String
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((this.activity?.application as LocationApplication).repository)
    }

    var database: DatabaseReference =
        FirebaseDatabase.getInstance("https://appericolo-23934-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users/" + Firebase.auth.currentUser?.uid.toString() + "/favContacts")
    private lateinit var contactViewModel: ContactViewModel



    companion object{
        var instance: LocationUpdatesClientFragment?=null
        fun getLocationUpdatesClientFragmentInstance():LocationUpdatesClientFragment  {
            return instance!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view= inflater.inflate(R.layout.fragment_location_updates_client, container, false)

        instance = this
        coordinate = arguments?.getParcelable("coordinate")!! //coordinate della destinazione
        arrivalTime = arguments?.getString("orarioArrivo").toString()
        val indirizzoDestinazione = arguments?.getString("indirizzo").toString()

        view.findViewById<TextView>(R.id.textViewTime).text = arrivalTime
        view.findViewById<TextView>(R.id.textViewAddress).text = indirizzoDestinazione

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_sharing) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireContext())






        view.findViewById<FloatingActionButton>(R.id.stop_sharing).setOnClickListener() {
            showSafeArrivalDialog("Vuoi interrompere la condivisione?")
        }



        return view
    }




    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
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
        showCurrentLocation()
        //mostra destinazione su mappa
        showPlaceOnMap(coordinate, mMap)
        updateLocation()
    }


    @SuppressLint("MissingPermission")
    override fun showCurrentLocation() {
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) {
            if(it != null){

                MapFragment.lastLocation = it
                val currentLatLng = LatLng(it.latitude, it.longitude)
                mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16F))
                placeMarkerOnMap(currentLatLng, mMap, R.drawable.icons8_street_view_48) //marker della persona posizionato all'inizio del percorso
            }
        }

    }

    //metodo per il display di un dialog quando l'utente vuole interrompere la condivisione;
    //Se l'utente decide di interrompere, i contatti stretti ricevono una notifica
    private fun showSafeArrivalDialog(message: String){
        contactViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)

        contactViewModel.readAllData.observeOnce {
            //Log.i("LocationUpdates", contactViewModel.readAllDataFromLocal.value?.size.toString())
        }

        val navController = NavHostFragment.findNavController(this)
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Safe arrival check")
            builder.setMessage(message)

            builder.apply {
                setPositiveButton("Sì",
                    DialogInterface.OnClickListener { dialog, id ->
                        val title = "Appericolo"
                        val message =
                            FirebaseAuth.getInstance().currentUser!!.email + " è arrivato a destinazione!"


                        // These registration tokens come from the client FCM SDKs.
                        database.parent?.parent?.get()?.addOnSuccessListener { users->
                            for (user in users.children){
                                //Log.i("contatti db remoto: ", user.child("name").getValue().toString())
                                for (favContact in contactViewModel.readAllData.value!!) {
                                    //Log.i("contatti in db locale: ", favContact.number.toString().replace(" ", ""))
                                    if (favContact.number.replace(" ", "") == user.child("cell_number").getValue().toString() ||
                                        favContact.number.replace(" ", "") == ("+39" + user.child("cell_number").getValue().toString()) ||
                                        ("+39" + favContact.number.replace(" ", "")) == user.child("cell_number").getValue().toString() ) {
                                        //Log.i("contatto buono", user.child("name").getValue().toString())

                                        val token = user.child("token").getValue().toString()
                                        //invia notifica al contatto stretto selezionato
                                        PushNotification(
                                            NotificationData(title,
                                                message,
                                                FirebaseAuth.getInstance().currentUser!!.uid, true, "", 0.0, 0.0),
                                            token,
                                            //TOPIC
                                        ).also {
                                            sendArrivalNotification(it)
                                        }
                                    }
                                }
                            }
                        }
                        /*
                        // These registration tokens come from the client FCM SDKs.

                        val recipientsTokens =
                            listOf(//"dCAS80DATU6chvEJ8gXXEp:APA91bFiIRG1rKXDNBn52LV2YZj9wulHqRQD9IdVAtqo31XJ9XUyxpZPGDFkqMke8_bU8GtIudhIGu-MS7oSlGJ_cN2mzCTsNGNK0gBMT5uhGe9f3PWG8xPwCVs0ivplim_Z5Fxu4Fbv",
                            "e3BKqdNETUCRBZMFf_4aeX:APA91bEYB7kPWdN57r0uDJ0tUXdnwRIM3ObArDqZvzP5B_CYZXZT0jXKe9_bSy2vUdpb91sEKCDWPR5fRfvlNzwMk5o0DS_tABTTBIo8YdPYSrRlzmCiNO6jZd2JvdCqNoEayCxQh1jk")
                        if (title.isNotEmpty() && message.isNotEmpty() && recipientsTokens.isNotEmpty()) {
                            for (token in recipientsTokens) {
                                PushNotification(
                                    NotificationData(title,
                                        message,
                                        FirebaseAuth.getInstance().currentUser!!.uid, true, "", 0.0, 0.0),
                                    token,
                                    //TOPIC
                                ).also {
                                    sendArrivalNotification(it)
                                }
                            }
                        }*/

                        navController.navigate(R.id.action_locationUpdatesClientFragment_to_navigation_dashboard)
                    })
                setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }

            // Create the AlertDialog
            builder.create()
            builder.show()
        }
    }
    override fun placeMarkerOnMap(currentLatLng: LatLng, mMap: GoogleMap, markerIconId: Int) {

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
        mMap.addMarker(markerOptions)
    }



    //Metodo che invia una notifica ai contatti stretti quando l'utente che condivide la propria posizione è arrivato
    private fun sendArrivalNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.i("LocationUpdateClient", "Response success")
            }else{
                Log.e("LocationUpdateClient", response.errorBody().toString())
            }
        }catch (e: Exception){
            Log.e("LocationUpdateClient", e.toString())
        }
    }



    @SuppressLint("MissingPermission")//non c'è bisogno di controllare i permessi poiché è gia stato fatto nella home
    private fun updateLocation(){
        buildLocationRequest()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        getPendingIntent()?.let { fusedLocationClient.requestLocationUpdates(locationRequest, it) }

    }

    private fun buildLocationRequest(){
        locationRequest = LocationRequest.create()
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000 //1000
        locationRequest.smallestDisplacement = 0f//10f

    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this.activity, MyLocationService::class.java)
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE)
        return PendingIntent.getBroadcast(this.activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT )
    }

    //metodo per l'aggiornamento continuo della posizione dell'utente in movimento sul database real-time
     fun updateFirebaseLocation(lastLocation: android.location.Location){
        this.activity?.runOnUiThread{

            //runOnUiThread runs the specified action on the UI thread. If the current thread is the UI thread,
            // then the action is executed immediately.
            // If the current thread is not the UI thread, the action is posted to the event queue of the UI thread.
            locationViewModel.insertCurrentLocation(lastLocation)
        }
    }

    //metodo che definisce un One Time Observer per i LiveData
    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }


}