
package com.example.appericolo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appericolo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




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

    override fun onBackPressed() {
        val currentFragment =
            findNavController(R.id.nav_host_fragment_activity_main).currentDestination?.id
        if (currentFragment == R.id.locationUpdatesClientFragment) {
            //se l'utente sta condividendo la posizione, non può usare il back button ma deve
                //prima interrompere la condivisione
            return
        }
        super.onBackPressed()
    }
}




