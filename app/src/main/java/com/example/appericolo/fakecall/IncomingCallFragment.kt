package com.example.appericolo.fakecall

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.appericolo.R
import com.example.appericolo.R.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment per accettare o rifiutare una chiamata
 */

class IncomingCallFragment : Fragment(){


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(layout.fragment_incoming_call, container, false)

        val refuseButton = view.findViewById<FloatingActionButton>(R.id.refuseCallButton)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        refuseButton.setOnClickListener{
            notificationManager.cancel(1000)
            activity?.finish()
        }

        val acceptButton = view.findViewById<FloatingActionButton>(R.id.acceptCallButton)
        acceptButton.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragment = CallAcceptedFragment()
            fragmentTransaction.replace(R.id.fullscreen_content, fragment).commit()
            //r.stop()
            notificationManager.cancel(HeadsUpNotificationService.mNotificationId)

        }
        return view
    }


}