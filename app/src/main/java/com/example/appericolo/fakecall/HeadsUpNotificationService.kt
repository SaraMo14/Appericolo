package com.example.appericolo.fakecall

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*


class HeadsUpNotificationService : IntentService("NotificationService") {

    private lateinit var mNotification: Notification
    //private val mNotificationId: Int = 1000

    @SuppressLint("NewApi")
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            val context = this.applicationContext
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.parseColor("#e8334a")
            notificationChannel.description = "fake call channel"//getString(R.string.notification_channel_description)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            //val r = RingtoneManager.getRingtone(applicationContext, ringtone)
            //r.isLooping = true
            //r.play()
            val att = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            notificationChannel.setSound(ringtone, att)

            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {
        const val CHANNEL_ID = "samples.notification.devdeeds.com.CHANNEL_ID2"
        const val CHANNEL_NAME = "Sample Notification"
        const val mNotificationId: Int = 1000

    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onHandleIntent(intent: Intent?) {
        //Create Channel
        createChannel()


        var timestamp: Long = 0
        if (intent != null && intent.extras != null) {
            timestamp = intent.extras!!.getLong("timestamp")
        }

        if (timestamp > 0) {
            val context = this.applicationContext
            var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notifyIntent = Intent(this, IncomingCallActivity::class.java)

            val number = intent?.getStringExtra("cellulare")
            val name = intent?.getStringExtra("nominativo")

            notifyIntent.putExtra("cellulare", number)
            notifyIntent.putExtra("nominativo", name)
            notifyIntent.putExtra("notification", true)

            notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp


            val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            val res = this.resources


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotification = Notification.Builder(this, CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    //.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.arrow_down_float) //RIVEDI
                    .setLargeIcon(BitmapFactory.decodeResource(res,
                        com.example.appericolo.R.drawable.icon_accept_call))
                    .setAutoCancel(true)
                    .setContentTitle("Chiamata in arrivo")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setStyle(Notification.BigTextStyle()
                        .bigText(name))
                    .setFullScreenIntent(pendingIntent, true)
                    .setContentText(number).build()
            } else {

                mNotification = Notification.Builder(this)
                    // Set the intent that will fire when the user taps the notification
                    //.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.arrow_down_float)//RIVEDI
                    .setLargeIcon(BitmapFactory.decodeResource(res,
                        com.example.appericolo.R.drawable.icon_accept_call))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Chiamata in arrivo")
                    .setStyle(Notification.BigTextStyle()
                        .bigText(name))
                    .setFullScreenIntent(pendingIntent, true)
                    .setContentText(number).build()

            }



            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)
        }


    }
}