
package com.example.appericolo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appericolo.ui.preferiti.luoghi.data.LocationApplication
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModel
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModelFactory
import com.example.appericolo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


const val TOPIC = "/topics/myTopic"


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val locationViewModel: LocationViewModel by viewModels {
        LocationViewModelFactory((this.application as LocationApplication).repository)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Subscribe the devices corresponding to the registration tokens to the topic.
        /*FirebaseService.sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if(result != null){
                FirebaseService.token = result
                Toast.makeText(this, "Server Token $result", Toast.LENGTH_LONG).show()
            }
        }*/


        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(com.example.appericolo.R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.example.appericolo.R.id.navigation_home, com.example.appericolo.R.id.navigation_dashboard, com.example.appericolo.R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        //Se l'utente avvia la condivisione della posizione, allora aggiorna la posizione ogni 10 sec
        if(intent.getStringExtra("code") == "start_live_location"){
            val args = intent.getBundleExtra("LocationsBundle")
            Log.i("MainActivity", args?.getString("orario_arrivo").toString())
            Toast.makeText(this, args?.getString("orario_arrivo").toString(), Toast.LENGTH_LONG).show()
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
            Log.i("MainActivity", "sono nella main activity")
            locationViewModel.insertCurrentLocation()
            Log.i("MainActivity", "sono nella main activity2")
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this, data?.getStringExtra("orario_arrivo"), Toast.LENGTH_LONG).show()
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
                locationViewModel.insertCurrentLocation()
                //val result = data!!.getIntExtra("result", 0)
                //textViewResult.text = "" + result
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //textViewResult.text = "Nothing selected"
            }
        }
    }*/

}



