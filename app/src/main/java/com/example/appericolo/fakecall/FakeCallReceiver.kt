package com.example.appericolo.fakecall

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

/**
 * Receiver per la ricezione dell'intent di una chiamata pianificata
 */
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

    }

}