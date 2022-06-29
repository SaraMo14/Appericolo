package com.example.appericolo.ui.preferiti.luoghi

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appericolo.R
import com.example.appericolo.ui.preferiti.luoghi.database.Location
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment contenuto nel tab 2 delle preferenze dell'utente. Esso consente all'utente di aggiungere, eliminare e visualizzare
 * i luoghi preferiti (destinazioni tipiche dell'utente)
 */
class ListaLuoghiFragment : Fragment(), LocationListAdapter.RowClickListener {
    private val newLocationActivityRequestCode = 1
    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((this.activity?.application as LocationApplication).repository)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_location_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = LocationListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())

        // Add an observer on the LiveData returned by getAllLocations.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        locationViewModel.allLocations.observe(this.requireActivity(), Observer { locations ->
            // Update the cached copy of the locations in the adapter.
            locations?.let { adapter.submitList(it) }
        })

        //per aggiungere un nuovo luogo
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this.requireContext(), NewLocationActivity::class.java)
            startActivityForResult(intent, newLocationActivityRequestCode)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newLocationActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val location = Location(0, intentData?.getStringExtra("indirizzo"), intentData?.getStringExtra("latitudine"), intentData?.getStringExtra("longitudine"))
            locationViewModel.insert(location)
        }/* else{
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }*/
    }

    override fun onDeleteLocationClickListener(location: Location) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        alertDialog.setTitle("Elimina luogo")
        alertDialog.setMessage("Sei sicuro di voler eliminare il luogo dalla lista dei preferiti?")
        alertDialog.setPositiveButton("ELIMINA",
            DialogInterface.OnClickListener { dialog, which -> locationViewModel.delete(location) })
        alertDialog.setNegativeButton("ANNULLA",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel()
            })

        val dialog: AlertDialog = alertDialog.create()
        dialog.show()

    }

    override fun onItemClickListener(location: Location) {
        //return
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        alertDialog.setTitle("Elimina luogo")
        alertDialog.setMessage("Sei sicuro di voler eliminare il luogo dalla lista dei preferiti?")
        alertDialog.setPositiveButton("ELIMINA",
            DialogInterface.OnClickListener { dialog, which -> locationViewModel.delete(location) })
        alertDialog.setNegativeButton("ANNULLA",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel()
            })

        val dialog: AlertDialog = alertDialog.create()
        dialog.show()

    }

}