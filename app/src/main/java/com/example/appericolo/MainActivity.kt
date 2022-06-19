
package com.example.appericolo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appericolo.ui.preferiti.luoghi.data.LocationApplication
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModel
import com.example.appericolo.ui.preferiti.luoghi.data.LocationViewModelFactory
import com.example.appericolo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


//const val TOPIC = "/topics/myTopic"


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

        //nascondi navView quando condividi la posizione
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.locationUpdatesClientFragment) {
                navView.visibility = View.GONE
            } else {
                navView.visibility = View.VISIBLE
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.example.appericolo.R.id.navigation_home, com.example.appericolo.R.id.navigation_dashboard, com.example.appericolo.R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.example.appericolo.R.id.navigation_home, com.example.appericolo.R.id.navigation_dashboard, com.example.appericolo.R.id.navigation_notifications
            )
        )
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}




