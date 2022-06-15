package com.example.appericolo.sharelocation.fcm

import com.google.android.gms.maps.model.LatLng

data class NotificationData(
    val title: String,
    val message: String,
    val senderUid: String
)
