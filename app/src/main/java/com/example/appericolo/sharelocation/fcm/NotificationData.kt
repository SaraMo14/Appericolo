package com.example.appericolo.sharelocation.fcm

import com.google.android.gms.maps.model.LatLng

data class NotificationData(
    val title: String,
    val message: String,
    val senderUid: String,
    val stopSharing: Boolean = false, // false -> notifica di inizio condivisione, true -> notifica di fine condivisione
    val arrivalTime: String,
    //val destination: LatLng?
    val destinationLat: Double,
    val destinationLong: Double
)
