package com.example.appericolo.sharelocation.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.appericolo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.util.Log
import com.example.appericolo.sharelocation.LocationUpdatesServerActivity

private const val CHANNEL_ID = "my_channel"

class FirebaseService : FirebaseMessagingService() {

   /* companion object {
        var sharedPref: SharedPreferences? = null

        var token: String?
            get() {
                //return sharedPref?.getString("token", "")
            }
            set(value) {
                //sharedPref?.edit()?.putString("token", value)?.apply()
            }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token = newToken
    }*/

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(this, LocationUpdatesServerActivity::class.java)
        val stopSharingFlag = message.data["stopSharing"]

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("senderUid", message.data["senderUid"])
        intent.putExtra("arrivalTime", message.data["arrivalTime"])
        intent.putExtra("destinationLat", message.data["destinationLat"])
        intent.putExtra("destinationLong", message.data["destinationLong"])
        intent.putExtra("stopSharing", stopSharingFlag)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "LiveLocationChannel"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "Channel to share live location"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

}





