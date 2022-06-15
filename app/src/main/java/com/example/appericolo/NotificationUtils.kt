package com.example.appericolo

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*


//This utility class will helps us to send notification by calling setNotification()
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