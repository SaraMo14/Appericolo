package com.example.appericolo.sharelocation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.appericolo.R
import com.example.appericolo.ui.preferiti.luoghi.LocationApplication
import com.example.appericolo.ui.preferiti.luoghi.LocationViewModel
import com.example.appericolo.ui.preferiti.luoghi.LocationViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment per la selezione da parte dell'utente della destinazione da raggiungere
 */

class SelectDestinationFragment : Fragment() {
    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((this.activity?.application as LocationApplication).repository)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_select_destination, container, false)


        setupAutoCompleteTextView()


        view.findViewById<FloatingActionButton>(R.id.load_destination_button).setOnClickListener{
            if (view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text == null || view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text.toString() == "") {
                Toast.makeText(this.activity?.applicationContext,"Inserire destinazione", Toast.LENGTH_SHORT).show()
            }
            else{
                val bundle = bundleOf("indirizzo" to view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text.toString())
                Navigation.findNavController(view).navigate(R.id.action_selectDestinationFragment_to_showDestinationFragment, bundle)
            }
        }
            return view
        }

    //metodo che mostra, mentre si scrive nella textview, i luoghi salvati tra i preferiti; è utile per facilitare e velocizzare
    //la scelta della destinazione
    private fun setupAutoCompleteTextView() {
        locationViewModel.allLocations.observe(this.requireActivity(), Observer { locations ->
            locations?.let {
                val arr = mutableListOf<String>()

                for (value in it) {
                    arr.add(value.indirizzo.toString())
                }
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter(this.requireContext(), android.R.layout.select_dialog_item, arr)
                view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)?.setAdapter(adapter)
            }
        })
    }

}