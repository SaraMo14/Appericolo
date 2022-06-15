package com.example.appericolo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CallAcceptedFragment : Fragment() {

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_call_accepted, container, false)
        val chronometer =view.findViewById<Chronometer>(R.id.c_meter)
        chronometer.start() // start a chronometer

        val refuseButton = view.findViewById<FloatingActionButton>(R.id.refuseCallButton)
        refuseButton.setOnClickListener {
            chronometer.stop() //serve?
            activity?.finish()
        }

        return view
    }


}