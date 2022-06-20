package com.example.appericolo

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log


private const val TAG = "PhoneCallReceiver"

class FakeCallReceiver : BroadcastReceiver() {

    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val service = Intent(context, HeadsUpNotificationService::class.java)
        service.putExtra("reason", intent?.getStringExtra("reason"))
        service.putExtra("timestamp", intent?.getLongExtra("timestamp", 0))
        service.putExtra("nominativo", intent?.getStringExtra("nominativo"))
        service.putExtra("cellulare", intent?.getStringExtra("cellulare"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(service)
        } else {
            context?.startService(service)
        }
        //context?.startService(service)
    /*if (intent?.action.equals("com.tester.alarmmanager")) {
            val pm = context?.getSystemService(POWER_SERVICE) as PowerManager
            val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG")
            wl.acquire()
            val new_intent = Intent(context, IncomingCallActivity::class.java)
            new_intent.putExtra("nominativo", intent?.getStringExtra("nominativo"))
            new_intent.putExtra("cellulare",   intent?.getStringExtra("cellulare"))
            new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context?.startActivity(new_intent)

            // Release the lock
            wl.release();
        } else if(intent?.action.equals("android.intent.action.BOOT_COMPLETED")){
            //Log.i("REBOOT",  "reboot")
        }*/

    }

}