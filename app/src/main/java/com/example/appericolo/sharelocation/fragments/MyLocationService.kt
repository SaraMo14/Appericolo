package com.example.appericolo.sharelocation.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.LocationResult
import java.lang.Exception
/**
 * Classe per la gestione degli aggiornamenti sulla posizione dell'utente e il relativo aggiornamento del database real-time
 */
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
                    try{
                        if (location != null) {
                            LocationUpdatesClientFragment.getLocationUpdatesClientFragmentInstance().updateFirebaseLocation(location)
                        }
                    }catch (e: Exception){
                        //Toast.makeText(context, location.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}