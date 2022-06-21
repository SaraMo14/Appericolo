package com.example.appericolo.utils

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.example.appericolo.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

/**
 * Interfaccia contenente metodi utili per la gestione delle mappe e della posizione
 */
interface MapsUtil {
    fun showPlaceOnMap(destinationCoordinates: LatLng, mMap: GoogleMap){
        placeMarkerOnMap(destinationCoordinates, mMap, 0)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(destinationCoordinates))
    }
    fun showCurrentLocation()
    fun placeMarkerOnMap(currentLatLng: LatLng, mMap: GoogleMap, markerIconId: Int = 0)


    companion object {
        fun isArrivalTimeExpired(stringTime: String): Boolean {

            val sdf = SimpleDateFormat("HH:mm")
            var time: Date? = null
            try{
                time = sdf.parse(stringTime)
            }catch (e: Exception){
                return true
            }

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


    }
}