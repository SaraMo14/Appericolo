package com.example.appericolo.sharelocation.fragments

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.appericolo.R
import com.example.appericolo.utils.MapsUtil
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.String
import java.util.*

/**
 * Fragment per la definizione di un orario stimato di arrivo per il viaggio da parte dell'utente
 */
class ArrivalTimeFragment : Fragment() {

    private var hour: Int = 0
    private var minute: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_arrival_time, container, false)
        //bundle del fragment ShowDestinationFragment
        val indirizzo = arguments?.getString("indirizzo").toString()
        val coordinate: LatLng? = arguments?.getParcelable("coordinate")
        view.findViewById<MaterialButton>(R.id.estimatedArrivalTime).setOnClickListener {
            popTimePicker()
        }
        //se clicco sulla freccia per proseguire:
        view.findViewById<FloatingActionButton>(R.id.forwardFab2).setOnClickListener {
            //se l'orario inserito supera l'ora attuale
            if(MapsUtil.isArrivalTimeExpired(view?.findViewById<Button>(R.id.estimatedArrivalTime)?.text.toString())){
                val alertDialog: AlertDialog? = this.let {
                        val builder = AlertDialog.Builder(it.requireContext())
                        builder.setTitle("Input error ")
                        builder.setMessage("Inserire un orario futuro a quello attuale.")

                        builder.apply {
                            setPositiveButton("Ho capito",
                                DialogInterface.OnClickListener { dialog, id ->
                                    //finish()
                                })
                        }

                        // Create the AlertDialog
                        builder.create()
                        builder.show()
                    }
            }else{
                val args = bundleOf("indirizzo" to indirizzo, "coordinate" to coordinate, "orario_arrivo" to view.findViewById<Button>(R.id.estimatedArrivalTime).text.toString())
                //Navigation.findNavController(view).navigate(R.id.action_arrivalTimeFragment_to_summaryFragment, args)
                val navController = NavHostFragment.findNavController(this)
                navController.navigate(R.id.action_arrivalTimeFragment_to_summaryFragment, args)

            }

        }
        return view
    }



    private fun popTimePicker() {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                view?.findViewById<Button>(R.id.estimatedArrivalTime)?.text = String.format(Locale.getDefault(),
                    "%02d:%02d",
                    hour,
                    minute)
            }

        val timePickerDialog =
            TimePickerDialog(this.context,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Seleziona orario")
        timePickerDialog.show()
    }

}