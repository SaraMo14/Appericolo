package com.example.appericolo.utils

import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*

interface MapsUtil {
    fun showDestination(destinationCoordinates: LatLng)
    fun showCurrentLocation()
    companion object {
        fun isArrivalTimeExpired(time: String): Boolean {

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


    }
}