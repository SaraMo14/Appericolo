package com.example.appericolo.sharelocation.fragments

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.appericolo.MainActivity
import com.example.appericolo.sharelocation.fragments.LocationUpdatesClientFragment.Companion.instance
import com.example.appericolo.ui.preferiti.luoghi.data.LocationApplication
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModel
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModelFactory
import com.google.android.gms.location.LocationResult
import java.lang.Exception

class MyLocationService : BroadcastReceiver() {

    companion object{
        val ACTION_PROCESS_UPDATE = "com.example.appericolo.sharelocation.fragments.UPDATE_LOCATION"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!=null){
            val action = intent!!.action
            if(action.equals(ACTION_PROCESS_UPDATE)){

                val result = LocationResult.extractResult(intent!!)
                if(result!= null){
                    val location = result.lastLocation
                    //val location_string = StringBuilder(location?.latitude.toString()).append("/").append(location?.longitude.toString()).toString()
                    try{
                        if (location != null) {
                            LocationUpdatesClientFragment.getLocationUpdatesClientFragmentInstance().updateFirebaseLocation(location)
                        }
                    }catch (e: Exception){
                        //if app is in killed mode
                        //Toast.makeText(context, location.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}