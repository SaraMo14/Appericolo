package com.example.appericolo.fakecall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import com.example.appericolo.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment che simula l'interfaccia di una chiamata dopo essere stata accettata
 */

class CallAcceptedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_call_accepted, container, false)
        val chronometer =view.findViewById<Chronometer>(R.id.c_meter)
        chronometer.start() // start a chronometer

        //per chiudere la chiamata:
        val refuseButton = view.findViewById<FloatingActionButton>(R.id.refuseCallButton)
        refuseButton.setOnClickListener {
            chronometer.stop() //serve?
            activity?.finish()
        }

        return view
    }


}