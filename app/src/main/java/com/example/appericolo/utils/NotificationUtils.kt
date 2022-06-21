package com.example.appericolo.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import com.example.appericolo.FakeCallReceiver
import java.util.*


/**
 * Utility class di supporto alla creazione della notifica di ricezione della chiamata fake
 */
class NotificationUtils {


    fun setNotification(timeInMilliSeconds: Long, activity: Activity, name: String, cell: String) {

        //------------  alarm settings start  -----------------//

        if (timeInMilliSeconds > 0) {


            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity.applicationContext, FakeCallReceiver::class.java) // AlarmReceiver1 = broadcast receiver

            alarmIntent.putExtra("reason", "notification")
            alarmIntent.putExtra("timestamp", timeInMilliSeconds)
            alarmIntent.putExtra("nominativo", name)
            alarmIntent.putExtra("cellulare", cell)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds

            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliSeconds, pendingIntent)

        }

        //------------ end of alarm settings  -----------------//


    }
}