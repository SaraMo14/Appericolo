package com.example.appericolo

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
import com.example.appericolo.CallAcceptedFragment
import com.example.appericolo.HeadsUpNotificationService
import com.example.appericolo.R
import com.example.appericolo.R.*
import com.google.android.material.floatingactionbutton.FloatingActionButton


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IncomingCallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IncomingCallFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(layout.fragment_incoming_call, container, false)
        //set ringtone
        /*val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val r = RingtoneManager.getRingtone(activity?.applicationContext, ringtone)
        r.isLooping = true
        r.play()*/
        val refuseButton = view.findViewById<FloatingActionButton>(R.id.refuseCallButton)
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        refuseButton.setOnClickListener{
            notificationManager?.cancel(1000)
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IncomingCallFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IncomingCallFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}