package com.example.appericolo.sharelocation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.appericolo.databinding.ActivitySelectDestinationBinding
import com.example.appericolo.ui.preferiti.luoghi.data.LocationApplication
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModel
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModelFactory


class SelectDestinationActivity : AppCompatActivity(){

    private lateinit var binding: ActivitySelectDestinationBinding
    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((application as LocationApplication).repository)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupAutoCompleteTextView()


        binding.loadDestinationButton.setOnClickListener{
            if (binding.autoCompleteTextView.text == null || binding.autoCompleteTextView.text.toString() == "") {
                Toast.makeText(applicationContext,"Inserire destinazione", Toast.LENGTH_SHORT).show()
            }
            else{
                val intent = Intent(this, ShowDestinationActivity::class.java)
                intent.putExtra("indirizzo", binding.autoCompleteTextView.text.toString())
                startActivity(intent)

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Activity.RESULT_OK && requestCode == 400) {
            //setResult(Activity.RESULT_OK, data)
            finish()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupAutoCompleteTextView() {
        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let {
                val arr = mutableListOf<String>()

                for (value in it) {
                    arr.add(value.indirizzo.toString())
                }
                val adapter: ArrayAdapter<String> =
                    ArrayAdapter(this, android.R.layout.select_dialog_item, arr)
                binding.autoCompleteTextView.setAdapter(adapter)
            }
        })
    }

}
